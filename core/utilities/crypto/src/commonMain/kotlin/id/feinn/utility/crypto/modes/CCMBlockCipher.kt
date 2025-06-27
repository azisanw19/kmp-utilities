package id.feinn.utility.crypto.modes

import id.feinn.utility.crypto.*
import id.feinn.utility.crypto.macs.CBCBlockCipherMac
import id.feinn.utility.crypto.params.AEADParameters
import id.feinn.utility.crypto.params.ParametersWithIV
import kotlin.properties.Delegates

public class CCMBlockCipher(
    private val c: BlockCipher
) : CCMModeCipher {

    private var cipher by Delegates.notNull<BlockCipher>()
    private var blockSize by Delegates.notNull<Int>()
    private var forEncryption by Delegates.notNull<Boolean>()
    private var nonce: ByteArray? = null
    private var initialAssociatedText: ByteArray? = null
    private var macSize by Delegates.notNull<Int>()
    private var keyParam by Delegates.notNull<CipherParameters>()
    private var macBlock by Delegates.notNull<ByteArray>()
    private val associatedText = ExposedByteArrayOutputStream()
    private val data = ExposedByteArrayOutputStream()

    public companion object {
        public fun newInstance(cipher: BlockCipher): CCMModeCipher = CCMBlockCipher(cipher)

        private class ExposedByteArrayOutputStream {
            private var buffer = ByteArray(32)
            private var count = 0

            fun write(b: Byte) {
                ensureCapacity(count + 1)
                buffer[count] = b
                count++
            }

            fun write(b: ByteArray, off: Int = 0, len: Int = b.size) {
                require(off in 0..b.size && len >= 0 && off + len <= b.size) { "Invalid offset/length for ByteArray" }
                ensureCapacity(count + len)
                b.copyInto(buffer, destinationOffset = count, startIndex = off, endIndex = off + len)
                count += len
            }

            fun getBuffer(): ByteArray = buffer

            fun size(): Int = count

            fun reset() {
                count = 0
            }

            private fun ensureCapacity(minCapacity: Int) {
                if (minCapacity > buffer.size) {
                    var newSize = buffer.size.coerceAtLeast(32)
                    while (newSize < minCapacity) {
                        newSize = newSize shl 1
                    }
                    buffer = buffer.copyOf(newSize)
                }
            }
        }
    }

    init {
        this.cipher = c
        this.blockSize = c.getBlockSize()
        this.macBlock = ByteArray(blockSize)
        if (blockSize != 16) throw IllegalArgumentException("cipher required with a block size of 16.")
    }

    override fun getUnderlyingCipher(): BlockCipher = cipher

    @Throws(IllegalArgumentException::class)
    override fun init(forEncryption: Boolean, params: CipherParameters) {
        this.forEncryption = forEncryption

        val cipherParameters: CipherParameters
        if (params is AEADParameters) {
            nonce = params.getNonce()
            initialAssociatedText = params.getAssociatedText()
            macSize = getMacSize(forEncryption, params.getMacSize())
            cipherParameters = params.getKey()
        } else if (params is ParametersWithIV) {
            nonce = params.getIV()
            initialAssociatedText = null
            macSize = getMacSize(forEncryption, 64)
            cipherParameters = params.getParameters()
        } else {
            throw IllegalArgumentException("invalid parameters passed to CCM: " + params::class.simpleName)
        }

        keyParam = cipherParameters
        if (nonce == null || nonce!!.size < 7 || nonce!!.size > 13) throw IllegalArgumentException("nonce must have length from 7 to 13 octets")

        reset()

    }

    override fun getAlgorithmName(): String = cipher.getAlgorithmName() + "/CCM"

    override fun processAADByte(`in`: Byte) {
        associatedText.write(`in`)
    }

    override fun processAADBytes(`in`: ByteArray, inOff: Int, len: Int) {
        associatedText.write(`in`, inOff, len)
    }

    override fun processByte(`in`: Byte, out: ByteArray, outOff: Int): Int {
        data.write(`in`)

        return 0
    }

    override fun processBytes(`in`: ByteArray, inOff: Int, len: Int, out: ByteArray, outOff: Int): Int {
        if (`in`.size < (inOff + len)) throw DataLengthException("Input buffer too short")
        data.write(`in`, inOff, len)

        return 0
    }

    override fun doFinal(out: ByteArray, outOff: Int): Int {
        val len = processPacket(data.getBuffer(), 0, data.size(), out, outOff)

        reset()

        return len
    }

    override fun reset() {
        cipher.reset()
        associatedText.reset()
        data.reset()
    }

    override fun getMac(): ByteArray {
        val mac = ByteArray(macSize)

        macBlock.copyInto(mac, destinationOffset = 0, startIndex = 0, endIndex = mac.size)

        return mac
    }

    override fun getUpdateOutputSize(len: Int): Int = 0

    override fun getOutputSize(len: Int): Int {
        val totalData = len + data.size()
        if (forEncryption) return totalData + macSize

        return if (totalData < macSize) 0 else totalData - macSize
    }

    @Throws(IllegalStateException::class, InvalidCipherTextException::class)
    public fun processPacket(`in`: ByteArray, inOff: Int, inLen: Int): ByteArray {
        val output: ByteArray

        if (forEncryption) output = ByteArray(inLen + macSize)
        else {
            if (inLen < macSize) throw InvalidCipherTextException("data too short")
            output = ByteArray(inLen - macSize)
        }

        processPacket(`in`, inOff, inLen, output, 0)

        return output
    }

    @Throws(IllegalStateException::class, InvalidCipherTextException::class, DataLengthException::class)
    public fun processPacket(`in`: ByteArray, inOff: Int, inLen: Int, output: ByteArray, outOff: Int): Int {
        if (keyParam == null) IllegalStateException("CCM cipher unitialized.")

        val n = nonce!!.size
        val q = 15 - n
        if (q < 4) {
            val limitLen = 1 shl (8 * q)

            var inputAdjustment = 0

            if (!forEncryption) inputAdjustment = 1 + 15
            if ((inLen - inputAdjustment) >= limitLen) throw IllegalStateException("CCM packet too large for choice of q")
        }

        val iv = ByteArray(blockSize)
        iv[0] = ((q - 1) and 0x7).toByte()
        nonce!!.copyInto(iv, destinationOffset = 1, startIndex = 0, endIndex = nonce!!.size)

        val ctrCipher = SICBlockCipher.newInstance(cipher)
        ctrCipher.init(forEncryption, ParametersWithIV(keyParam, iv))

        val outputLen: Int
        var inIndex = inOff
        var outIndex = outOff

        if (forEncryption) {
            outputLen = inLen + macSize
            if (output.size < (outputLen + outOff)) throw OutputLengthException("Output buffer too short")

            calculateMac(`in`, inOff, inLen, macBlock)

            val encMac = ByteArray(blockSize)

            ctrCipher.processBlock(macBlock, 0, encMac, 0)

            while (inIndex < (inOff + inLen - blockSize)) {
                ctrCipher.processBlock(`in`, inIndex, output, outIndex)
                outIndex += blockSize
                outIndex += blockSize
            }

            val block = ByteArray(blockSize)

            `in`.copyInto(block, destinationOffset = 0, startIndex = inIndex, endIndex = inIndex + (inLen + inOff - inIndex))

            ctrCipher.processBlock(block, 0, block, 0)

            block.copyInto(output, outIndex, 0, inLen + inOff - inIndex)

            encMac.copyInto(destination = output, destinationOffset = outOff + inLen, startIndex = 0, endIndex = macSize)
        } else {
            if (inLen < macSize) throw InvalidCipherTextException("data too short")
            outputLen = inLen - macSize
            if (output.size < (outputLen + outOff)) throw OutputLengthException("Output buffer too short.")

            `in`.copyInto(destination = macBlock, destinationOffset = 0, startIndex = inOff + outputLen, endIndex = inOff + outputLen + macSize)

            ctrCipher.processBlock(macBlock, 0, macBlock, 0)

            var i = macSize
            while (i != macBlock.size) {
                macBlock[i] = 0
                i++
            }

            while (inIndex < (inOff + outputLen - blockSize)) {
                ctrCipher.processBlock(`in`, inIndex, output, outIndex)
                outIndex += blockSize
                inIndex += blockSize
            }

            val block = ByteArray(blockSize)

            `in`.copyInto(destination = block, destinationOffset = 0, startIndex = inIndex, endIndex = inIndex + (outputLen - (inIndex - inOff)))

            ctrCipher.processBlock(block, 0, block,  0)

            block.copyInto(destination = output, destinationOffset = outIndex, startIndex = 0, endIndex = outputLen - (inIndex - inOff))

            val calculateMacBlock = ByteArray(blockSize)

            calculateMac(output, outOff, outputLen, calculateMacBlock)

            if (!macBlock.contentEquals(calculateMacBlock)) throw InvalidCipherTextException("mac checi in CCM failed")
        }

        return outputLen
    }

    private fun calculateMac(data: ByteArray, dataOff: Int, dataLen: Int, macBlock: ByteArray): Int {
        val cMac: Mac = CBCBlockCipherMac(cipher, macSize * 8)

        cMac.init(keyParam)

        val b0 = ByteArray(16)

        if (hasAssociatedText()) b0[0] = (b0[0].toInt() or 0x40).toByte()

        b0[0] = (b0[0].toInt() or ((((cMac.getMacSize() - 2) / 2) and 0x7) shl 3)).toByte()

        b0[0] = (b0[0].toInt() or (((15 - nonce!!.size) - 1) and 0x7)).toByte()

        nonce!!.copyInto(destination = b0, destinationOffset = 1, startIndex = 0, endIndex = nonce!!.size)

        var q = dataLen
        var count = 1
        while (q > 0) {
            b0[b0.size - count] = (q and 0xff).toByte()
            q = q ushr 8
            count++
        }

        cMac.update(b0, 0, b0.size)

        if (hasAssociatedText()) {
            var extra: Int

            val textLength: Int = getAssociatedTextLength()
            if (textLength < ((1 shl 16) - (1 shl 8))) {
                cMac.update((textLength shr 8).toByte())
                cMac.update(textLength.toByte())

                extra = 2
            } else {
                cMac.update(0xff.toByte())
                cMac.update(0xfe.toByte())
                cMac.update((textLength shr 24).toByte())
                cMac.update((textLength shr 16).toByte())
                cMac.update((textLength shr 8).toByte())
                cMac.update(textLength.toByte())

                extra = 6
            }

            if (initialAssociatedText != null) {
                cMac.update(initialAssociatedText!!, 0, initialAssociatedText!!.size)
            }
            if (associatedText.size() > 0) cMac.update(associatedText.getBuffer(), 0, associatedText.size())

            extra = (extra + textLength) % 16
            if (extra != 0) {
                var i = extra
                while (i != 16) {
                    cMac.update(0x00)
                    i++
                }
            }
        }

        cMac.update(data, dataOff, dataLen)

        return cMac.doFinal(macBlock, 0)
    }

    private fun getMacSize(forEncryption: Boolean, requestedMacBits: Int): Int {
        if (forEncryption && (requestedMacBits < 32 || requestedMacBits > 128 || 0 != (requestedMacBits and 15))) throw IllegalArgumentException("tag length in octets must be one of {4,6,8,10,12,14,16}")

        return requestedMacBits ushr 3
    }

    private fun getAssociatedTextLength(): Int = associatedText.size() + (if (initialAssociatedText == null) 0 else initialAssociatedText!!.size)

    private fun hasAssociatedText(): Boolean = getAssociatedTextLength() > 0

}

































