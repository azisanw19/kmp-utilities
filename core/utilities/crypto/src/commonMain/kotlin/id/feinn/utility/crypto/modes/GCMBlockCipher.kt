package id.feinn.utility.crypto.modes

import id.feinn.utility.crypto.*
import id.feinn.utility.crypto.modes.gcm.*
import id.feinn.utility.crypto.params.AEADParameters
import id.feinn.utility.crypto.params.KeyParameter
import id.feinn.utility.crypto.params.ParametersWithIV
import id.feinn.utility.crypto.util.Pack
import kotlin.math.min
import kotlin.properties.Delegates

public class GCMBlockCipher: GCMModeCipher {

    public companion object {

        private const val BLOCK_SIZE = 16

        public fun newInstance(cipher: BlockCipher): GCMBlockCipher = GCMBlockCipher(cipher)

        public fun newInstance(cipher: BlockCipher, m: GCMMultiplier): GCMBlockCipher = GCMBlockCipher(cipher, m)

    }

    private val cipher: BlockCipher
    private var multiplier: GCMMultiplier
    private var exp: GCMExponentiator? = null

    private var forEncryption by Delegates.notNull<Boolean>()
    private var initialised by Delegates.notNull<Boolean>()
    private var macSize by Delegates.notNull<Int>()
    private var lastKey by Delegates.notNull<ByteArray>()
    private var nonce: ByteArray? = null
    private var initialAssociatedText: ByteArray? = null
    private var H by Delegates.notNull<ByteArray>()
    private var J0 by Delegates.notNull<ByteArray>()

    private var bufBlock by Delegates.notNull<ByteArray>()
    private var macBlock: ByteArray? = null
    private var S by Delegates.notNull<ByteArray>()
    private var S_at by Delegates.notNull<ByteArray>()
    private var S_atPre by Delegates.notNull<ByteArray>()
    private var counter by Delegates.notNull<ByteArray>()
    private var blocksRemaining by Delegates.notNull<Int>()
    private var bufOff by Delegates.notNull<Int>()
    private var totalLength by Delegates.notNull<Long>()
    private var atBlock by Delegates.notNull<ByteArray>()
    private var atBlockPos by Delegates.notNull<Int>()
    private var atLength by Delegates.notNull<Long>()
    private var atLengthPre by Delegates.notNull<Long>()

    public constructor(c: BlockCipher): this(c, null)

    public constructor(c: BlockCipher, m: GCMMultiplier?) {
        var mT = m
        if (c.getBlockSize() != BLOCK_SIZE) throw IllegalArgumentException("cipher required with a block size of $BLOCK_SIZE.")
        if (mT == null) mT = Tables4kGCMMultiplier()

        this.cipher = c
        this.multiplier = mT
    }

    override fun getUnderlyingCipher(): BlockCipher = cipher

    override fun getAlgorithmName(): String = cipher.getAlgorithmName() + "/GCM"

    @Throws(IllegalArgumentException::class)
    override fun init(forEncryption: Boolean, params: CipherParameters) {
        this.forEncryption = forEncryption
        this.macBlock = null
        this.initialised = true

        val keyParam: KeyParameter
        var newNonce: ByteArray? = null

        if (params is AEADParameters) {
            newNonce = params.getNonce()
            initialAssociatedText = params.getAssociatedText()

            val macSizeBits = params.getMacSize()
            if (macSizeBits < 32 || macSizeBits > 128 || macSizeBits % 8 != 0) throw IllegalArgumentException("Invalid value for MAC size: $macSizeBits")

            macSize = macSizeBits / 8
            keyParam = params.getKey()
        } else if (params is ParametersWithIV) {
            newNonce = params.getIV()
            initialAssociatedText = null
            macSize = 16
            keyParam = params.getParameters() as KeyParameter
        } else throw IllegalArgumentException("invalid parameters passed to GCM")

        val bufLength = if (forEncryption) BLOCK_SIZE else BLOCK_SIZE + macSize
        this.bufBlock = ByteArray(bufLength)

        if (newNonce.size < 1) throw IllegalArgumentException("IV must be at least 1 byte")

        if (forEncryption) {
            if (nonce != null && nonce.contentEquals(newNonce)) {
                if (lastKey.contentEquals(keyParam.getKey())) throw IllegalArgumentException("cannot reuse nonce for GCM encryption")
            }
        }

        nonce = newNonce
        lastKey = keyParam.getKey()

        cipher.init(true, keyParam)

        this.H = ByteArray(BLOCK_SIZE)
        cipher.processBlock(H, 0, H, 0)

        multiplier.init(H)
        exp = null

        this.J0 = ByteArray(BLOCK_SIZE)

        if (nonce!!.size == 12) {
            nonce!!.copyInto(J0, destinationOffset = 0, startIndex = 0, endIndex = nonce!!.size)
            this.J0[BLOCK_SIZE - 1] = 0x01
        } else {
            gHASH(J0, nonce!!, nonce!!.size)
            val X = ByteArray(BLOCK_SIZE)
            Pack.longToBigEndian((nonce!!.size * 8).toLong(), X, 8)
            gHASHBlock(J0, X)
        }

        this.S = ByteArray(BLOCK_SIZE)
        this.S_at = ByteArray(BLOCK_SIZE)
        this.S_atPre = ByteArray(BLOCK_SIZE)
        this.atBlock = ByteArray(BLOCK_SIZE)
        this.atBlockPos = 0
        this.atLength = 0
        this.atLengthPre = 0
        this.counter = J0.copyOf()
        this.blocksRemaining = -2
        this.bufOff = 0
        this.totalLength = 0

        if (initialAssociatedText != null) processAADBytes(initialAssociatedText!!, 0, initialAssociatedText!!.size)
    }

    override fun getMac(): ByteArray {
        if (macBlock == null) return ByteArray(macSize)
        return macBlock!!.copyOf()
    }

    override fun getOutputSize(len: Int): Int {
        val totalData = len + bufOff

        if (forEncryption) return totalData + macSize
        return if (totalData < macSize) 0 else totalData - macSize
    }

    override fun getUpdateOutputSize(len: Int): Int {
        var totalData = len + bufOff
        if (!forEncryption) {
            if (totalData < macSize) return 0
            totalData -= macSize
        }
        return totalData - totalData % BLOCK_SIZE
    }

    override fun processAADByte(`in`: Byte) {
        checkStatus()

        atBlock[atBlockPos] = `in`
        if (++atBlockPos == BLOCK_SIZE) {
            gHASHBlock(S_at, atBlock)
            atBlockPos = 0
            atLength += BLOCK_SIZE
        }
    }

    override fun processAADBytes(`in`: ByteArray, inOff: Int, len: Int) {
        var inOffT = inOff
        var lenT = len
        checkStatus()

        if (atBlockPos > 0) {
            val available = BLOCK_SIZE - atBlockPos
            if (lenT < available) {
                `in`.copyInto(destination = atBlock, destinationOffset = atBlockPos, startIndex = inOffT, endIndex = inOffT + lenT)
                atBlockPos += lenT
                return
            }

            `in`.copyInto(destination = atBlock, destinationOffset = atBlockPos, startIndex = inOffT, endIndex = inOffT + available)
            gHASHBlock(S_at, atBlock)
            atLength += BLOCK_SIZE
            inOffT += available
            lenT -= available
        }

        val inLimit = inOffT + lenT - BLOCK_SIZE

        while (inOffT < inLimit) {
            gHASHBlock(S_at, `in`, inOffT)
            atLength += BLOCK_SIZE
            inOffT += BLOCK_SIZE
        }

        atBlockPos = BLOCK_SIZE + inLimit - inOffT
        `in`.copyInto(destination = atBlock, destinationOffset = 0, startIndex = inOffT, endIndex = inOffT + atBlockPos)
    }

    private fun initCipher() {
        if (atLength > 0) {
            S_at.copyInto(destination = S_atPre, destinationOffset = 0, startIndex = 0, endIndex = BLOCK_SIZE)
            atLengthPre = atLength
        }

        if (atBlockPos > 0) {
            gHASHPartial(S_atPre, atBlock, 0, atBlockPos)
            atLengthPre += atBlockPos
        }

        if (atLengthPre > 0) S_atPre.copyInto(destination = S, destinationOffset = 0, startIndex = 0, endIndex = BLOCK_SIZE)
    }

    @Throws(DataLengthException::class)
    override fun processByte(`in`: Byte, out: ByteArray, outOff: Int): Int {
        checkStatus()

        bufBlock[bufOff] = `in`
        if (++bufOff == bufBlock.size) {
            if (forEncryption) {
                encryptBlock(bufBlock, 0, out, outOff)
                bufOff = 0
            } else {
                decryptBlock(bufBlock, 0, out, outOff)
                bufBlock.copyInto(destination = bufBlock, destinationOffset = 0, startIndex = BLOCK_SIZE, endIndex = BLOCK_SIZE + macSize)
                bufOff = macSize
            }
            return BLOCK_SIZE
        }
        return 0
    }

    @Throws(DataLengthException::class)
    override fun processBytes(`in`: ByteArray, inOff: Int, len: Int, out: ByteArray, outOff: Int): Int {
        var inOffT = inOff
        var lenT = len
        checkStatus()

        if ((`in`.size - inOffT) < lenT) throw DataLengthException("Input buffer too short")

        var resultLen = 0

        if (forEncryption) {
            if (bufOff > 0) {
                val available = BLOCK_SIZE - bufOff
                if (lenT < available) {
                    `in`.copyInto(destination = bufBlock, destinationOffset = bufOff, startIndex = inOffT, endIndex = inOffT + lenT)
                    bufOff += lenT
                    return 0
                }

                `in`.copyInto(destination = bufBlock, destinationOffset = bufOff, startIndex = inOffT, endIndex = inOffT + available)
                encryptBlock(bufBlock, 0, out, outOff)
                inOffT += available
                lenT -= available
                resultLen = BLOCK_SIZE

            }

            val inLimit = inOffT + lenT - BLOCK_SIZE

            while (inOffT <= inLimit) {
                encryptBlock(`in`, inOffT, out, outOff + resultLen)
                inOffT += BLOCK_SIZE
                resultLen += BLOCK_SIZE
            }

            bufOff = BLOCK_SIZE + inLimit - inOffT
            `in`.copyInto(destination = bufBlock, destinationOffset = 0, startIndex = inOffT, endIndex = inOffT + bufOff)
        } else {
            var available = bufBlock.size - bufOff
            if (lenT < available) {
                `in`.copyInto(destination = bufBlock, destinationOffset = bufOff, startIndex = inOffT, endIndex = inOffT + lenT)
                bufOff += lenT
                return 0
            }

            if (bufOff >= BLOCK_SIZE) {
                decryptBlock(bufBlock, 0, out, outOff)
                bufOff -= BLOCK_SIZE
                bufBlock.copyInto(destination = bufBlock, destinationOffset = 0, startIndex = BLOCK_SIZE, endIndex = BLOCK_SIZE + bufOff)
                resultLen = BLOCK_SIZE

                available += BLOCK_SIZE
                if (len < available) {
                    `in`.copyInto(destination = bufBlock, destinationOffset = bufOff, startIndex = inOffT, endIndex = inOffT + lenT)
                    bufOff += lenT
                    return resultLen
                }
            }

            val inLimit = inOffT + lenT - bufBlock.size

            available = BLOCK_SIZE - bufOff
            `in`.copyInto(destination = bufBlock, destinationOffset = bufOff, startIndex = inOffT, endIndex = inOffT + available)
            decryptBlock(bufBlock, 0, out, outOff + resultLen)
            inOffT += available
            resultLen += BLOCK_SIZE

            while (inOffT < inLimit) {
                decryptBlock(`in`, inOffT, out, outOff + resultLen)
                inOffT += BLOCK_SIZE
                resultLen += BLOCK_SIZE
            }

            bufOff = bufBlock.size + inLimit - inOffT
            `in`.copyInto(destination = bufBlock, destinationOffset = 0 ,startIndex = inOffT, endIndex = inOffT + bufOff)
        }

        return resultLen
    }

    override fun doFinal(out: ByteArray, outOff: Int): Int {
        checkStatus()

        if (totalLength == 0L) initCipher()

        var extra = bufOff

        if (forEncryption) {
            if ((out.size - outOff) < (extra + macSize)) throw OutputLengthException("Output buffer too short")
        } else {
            if (extra < macSize) throw InvalidCipherTextException("data too short")
            extra -= macSize

            if ((out.size - outOff) < extra) throw  OutputLengthException("Output buffer too short")
        }

        if (extra > 0) processPartial(bufBlock, 0, extra, out, outOff)

        atLength += atBlockPos

        if (atLength > atLengthPre) {
            if (atBlockPos > 0) gHASHPartial(S_at, atBlock, 0, atBlockPos)

            if (atLengthPre > 0) GCMUtil.xor(S_at, S_atPre)

            val c = ((totalLength * 8) + 127) ushr 7

            val H_c = ByteArray(16)
            if (exp == null) {
                exp = BasicGCMExponentiator()
                exp!!.init(H)
            }
            exp!!.exponentiateX(c, H_c)

            GCMUtil.multiply(S_at, H_c)

            GCMUtil.xor(S, S_at)
        }

        val X = ByteArray(BLOCK_SIZE)
        Pack.longToBigEndian(atLength * 8, X, 0)
        Pack.longToBigEndian(totalLength * 8, X, 8)

        gHASHBlock(S, X)

        val tag = ByteArray(BLOCK_SIZE)
        cipher.processBlock(J0, 0, tag, 0)
        GCMUtil.xor(tag, S)

        var resultLen = extra

        this.macBlock = ByteArray(macSize)
        tag.copyInto(destination = macBlock!!, destinationOffset = 0, startIndex = 0, endIndex = macSize)

        if (forEncryption) {
            macBlock!!.copyInto(destination = out, destinationOffset = outOff + bufOff, startIndex = 0, endIndex = macSize)
            resultLen += macSize
        } else {
            val msgMac = ByteArray(macSize)
            bufBlock.copyInto(destination = msgMac, destinationOffset = 0, startIndex = extra, endIndex = extra + macSize)
            if (!this.macBlock!!.contentEquals(msgMac)) throw InvalidCipherTextException("mac check in GCM failed")
        }

        reset(false)

        return resultLen
    }

    override fun reset() {
        reset(true)
    }

    private fun reset(clearMac: Boolean) {
        cipher.reset()

        S = ByteArray(BLOCK_SIZE)
        S_at = ByteArray(BLOCK_SIZE)
        S_atPre = ByteArray(BLOCK_SIZE)
        atBlock = ByteArray(BLOCK_SIZE)
        atBlockPos = 0
        atLength = 0
        atLengthPre = 0
        counter = J0.copyOf()
        blocksRemaining = -2
        bufOff = 0
        totalLength = 0

        bufBlock.fill(0)

        if (clearMac) macBlock = null

        if (forEncryption) initialised = false
        else {
            if (initialAssociatedText != null) {
                processAADBytes(initialAssociatedText!!, 0, initialAssociatedText!!.size)
            }
        }
    }

    private fun decryptBlock(buf: ByteArray, bufOff: Int, out: ByteArray, outOff: Int) {
        if ((out.size - outOff) < BLOCK_SIZE) throw OutputLengthException("Output buffer too short")
        if (totalLength == 0L) initCipher()

        val ctrBlock = ByteArray(BLOCK_SIZE)
        getNextCTRBlock(ctrBlock)

        gHASHBlock(S, buf, bufOff)
        GCMUtil.xor(ctrBlock, 0, buf, bufOff, out, outOff)

        totalLength += BLOCK_SIZE
    }

    private fun encryptBlock(buf: ByteArray, bufOff: Int, out: ByteArray, outOff: Int) {
        if ((out.size - outOff) < BLOCK_SIZE) throw OutputLengthException("Output buffer too short")
        if (totalLength == 0L) initCipher()

        val ctrBlock = ByteArray(BLOCK_SIZE)

        getNextCTRBlock(ctrBlock)
        GCMUtil.xor(ctrBlock, buf, bufOff)
        gHASHBlock(S, ctrBlock)
        ctrBlock.copyInto(destination = out, destinationOffset = outOff, startIndex = 0, endIndex = BLOCK_SIZE)

        totalLength += BLOCK_SIZE
    }

    private fun processPartial(buf: ByteArray, off: Int, len: Int, out: ByteArray, outOff: Int) {
        val ctrBlock = ByteArray(BLOCK_SIZE)
        getNextCTRBlock(ctrBlock)

        if (forEncryption) {
            GCMUtil.xor(buf, off, ctrBlock, 0, len)
            gHASHPartial(S, buf, off, len)
        } else {
            gHASHPartial(S, buf, off, len)
            GCMUtil.xor(buf, off, ctrBlock, 0, len)
        }

        buf.copyInto(destination = out, destinationOffset = outOff, startIndex = off, endIndex = off + len)
        totalLength += len
    }

    private fun gHASH(Y: ByteArray, b: ByteArray, len: Int) {
        for (pos in 0 until len step BLOCK_SIZE) {
            val num = min(len - pos, BLOCK_SIZE)
            gHASHPartial(Y, b, pos, num)
        }
    }

    private fun gHASHBlock(Y: ByteArray, b: ByteArray) {
        GCMUtil.xor(Y, b)
        multiplier.multiplyH(Y)
    }

    private fun gHASHBlock(Y: ByteArray, b: ByteArray, off: Int) {
        GCMUtil.xor(Y, b, off)
        multiplier.multiplyH(Y)
    }

    private fun gHASHPartial(Y: ByteArray, b: ByteArray, off: Int, len: Int) {
        GCMUtil.xor(Y, b, off, len)
        multiplier.multiplyH(Y)
    }

    private fun getNextCTRBlock(block: ByteArray) {
        if (blocksRemaining == 0) throw IllegalStateException("Attempt to process too many blocks")
        blocksRemaining--

        var c = 1
        c += counter[15].toInt() and 0xFF; counter[15] = c.toByte(); c = c ushr 8
        c += counter[14].toInt() and 0xFF; counter[14] = c.toByte(); c = c ushr 8
        c += counter[13].toInt() and 0xFF; counter[13] = c.toByte(); c = c ushr 8
        c += counter[12].toInt() and 0xFF; counter[12] = c.toByte()

        cipher.processBlock(counter, 0, block, 0)
    }

    private fun checkStatus() {
        if (!initialised) {
            if (forEncryption) throw IllegalStateException("GCM cipher cannot be reused for encryption")
            throw IllegalStateException("GCM cipher needs to be initialised")
        }
    }
}









































