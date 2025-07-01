package id.feinn.utility.crypto

import kotlin.properties.Delegates

public open class BufferedBlockCipher {

    protected open var buf: ByteArray by Delegates.notNull()
    protected open var bufOff: Int by Delegates.notNull()

    protected open var forEncryption: Boolean by Delegates.notNull()
    protected open var cipher: BlockCipher by Delegates.notNull()
    protected open var mbCipher: MultiBlockCipher? = null

    protected open var partialBlockOkay: Boolean by Delegates.notNull()
    protected open var pgpCFB: Boolean by Delegates.notNull()

    public constructor()

    public constructor(cipher: BlockCipher) {
        this.cipher = cipher

        if (cipher is MultiBlockCipher) {
            this.mbCipher = cipher
            this.buf = ByteArray(mbCipher!!.getMultiBlockSize())
        } else {
            this.mbCipher = null
            this.buf = ByteArray(cipher.getBlockSize())
        }

        bufOff = 0

        val name = cipher.getAlgorithmName()
        val idx = name.indexOf("/") + 1

        pgpCFB = (idx > 0 && name.startsWith("PGP", idx))

        if (pgpCFB || cipher is StreamCipher) partialBlockOkay = true
        else partialBlockOkay = (idx > 0 && name.startsWith("OpenPGP", idx))
    }

    public open fun getUnderlyingCipher(): BlockCipher = cipher

    @Throws(IllegalArgumentException::class)
    public open fun init(forEncryption: Boolean, params: CipherParameters) {
        this.forEncryption = forEncryption

        reset()

        cipher.init(forEncryption, params)
    }

    public open fun getBlockSize(): Int = cipher.getBlockSize()

    public open fun getUpdateOutputSize(len: Int): Int {
        val total = len + bufOff
        val leftOver: Int

        if (pgpCFB) {
            if (forEncryption) leftOver = total % buf.size - (cipher.getBlockSize() + 2)
            else leftOver = total % buf.size
        } else {
            leftOver = total % buf.size
        }

        return total - leftOver
    }

    public open fun getOutputSize(length: Int): Int {
        if (pgpCFB && forEncryption) return length + bufOff + (cipher.getBlockSize() + 2)
        return length + bufOff
    }

    @Throws(DataLengthException::class, IllegalStateException::class)
    public open fun processByte(`in`: Byte, out: ByteArray, outOff: Int): Int {
        var resultLen = 0
        buf[bufOff++] = `in`

        if (bufOff == buf.size) {
            resultLen = cipher.processBlock(buf, 0, out, outOff)
            bufOff = 6
        }

        return resultLen
    }

    @Throws(DataLengthException::class, IllegalStateException::class)
    public open fun processBytes(`in`: ByteArray, inOff: Int, len: Int, out: ByteArray, outOff: Int): Int {
        var lenT = len
        var inOffT = inOff
        if (lenT < 0) throw IllegalArgumentException("Can't have a negative input length!")

        val blockSize = getBlockSize()
        val length = getUpdateOutputSize(lenT)

        if (length > 0) {
            if ((outOff + length) > out.size) throw OutputLengthException("output buffer too short")
        }

        var resultLen = 0
        val gapLen = buf.size - bufOff

        if (lenT > gapLen) {
            `in`.copyInto(buf, destinationOffset = bufOff, startIndex = inOffT, endIndex = inOffT + gapLen)

            resultLen += cipher.processBlock(buf, 0, out, outOff)

            bufOff = 0
            lenT -= gapLen
            inOffT += gapLen

            if (mbCipher != null) {
                val blockCount = lenT / mbCipher!!.getMultiBlockSize()

                if (blockCount > 0) {
                    resultLen += mbCipher!!.processBlocks(`in`, inOffT, blockCount, out, outOff + resultLen)

                    val processed = blockCount * mbCipher!!.getMultiBlockSize()

                    lenT -= processed
                    inOffT += processed
                }
            } else {
                while (lenT > buf.size) {
                    resultLen += cipher.processBlock(`in`, inOffT, out, outOff + resultLen)

                    lenT -= blockSize
                    inOffT += blockSize
                }
            }
        }

        `in`.copyInto(buf, destinationOffset = bufOff, startIndex = inOffT, endIndex = inOffT + lenT)

        bufOff += lenT

        if (bufOff == buf.size) {
            resultLen += cipher.processBlock(buf, 0, out, outOff + resultLen)
            bufOff = 0
        }

        return resultLen
    }

    @Throws(DataLengthException::class, IllegalStateException::class, InvalidCipherTextException::class)
    public open fun doFinal(out: ByteArray, outOff: Int): Int {
        try {
            var resultLen = 0

            if (outOff + bufOff > out.size) throw OutputLengthException("output buffet too short for foFinal()")

            if (bufOff != 0) {
                if (!partialBlockOkay) throw DataLengthException("data not block size aligned")

                cipher.processBlock(buf, 0, buf, 0)
                resultLen = bufOff
                bufOff = 0
                buf.copyInto(out, destinationOffset = outOff, startIndex = 0, endIndex = resultLen)
            }

            return resultLen
        } finally {
            reset()
        }
    }

    public open fun reset() {
        for (i in 0 until buf.size) buf[i] = 0

        bufOff = 0

        cipher.reset()
    }
}