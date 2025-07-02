package id.feinn.utility.crypto.paddings

import id.feinn.utility.crypto.BlockCipher
import id.feinn.utility.crypto.CipherParameters
import id.feinn.utility.crypto.DataLengthException
import id.feinn.utility.crypto.DefaultBufferedBlockCipher
import id.feinn.utility.crypto.OutputLengthException
import id.feinn.utility.crypto.params.ParametersWithRandom
import kotlin.math.max

public class PaddedBufferedBlockCipher : DefaultBufferedBlockCipher {
    public val padding: BlockCipherPadding

    public constructor(cipher: BlockCipher, padding: BlockCipherPadding): super() {
        this.cipher = cipher
        this.padding = padding

        buf = ByteArray(cipher.getBlockSize())
        bufOff = 0
    }

    public constructor(cipher: BlockCipher): this(cipher, PKCS7Padding())

    override fun init(forEncryption: Boolean, params: CipherParameters) {
        this.forEncryption = forEncryption

        reset()

        if (params is ParametersWithRandom) {
            padding.init(params.getRandom())

            cipher.init(forEncryption, params.getParameters())
        } else {
            padding.init(null)

            cipher.init(forEncryption, params)
        }
    }

    override fun getOutputSize(length: Int): Int {
        val total = length + bufOff
        val leftOver = total % buf.size

        if (leftOver == 0) {
            if (forEncryption) return total + buf.size
            return total
        }

        return total - leftOver + buf.size
    }

    override fun getUpdateOutputSize(len: Int): Int {
        val total = len + bufOff
        val leftOver = total % buf.size

        if (leftOver == 0) return max(0, total - buf.size)
        return total - leftOver
    }

    override fun processByte(`in`: Byte, out: ByteArray, outOff: Int): Int {
        var resultLen = 0

        if (bufOff == buf.size) {
            resultLen = cipher.processBlock(buf, 0, out, outOff)
            bufOff = 0
        }

        buf[bufOff++] = `in`

        return resultLen
    }

    override fun processBytes(`in`: ByteArray, inOff: Int, len: Int, out: ByteArray, outOff: Int): Int {
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

            while (lenT > buf.size) {
                resultLen += cipher.processBlock(`in`, inOffT, out, outOff + resultLen)

                lenT -= blockSize
                inOffT += blockSize
            }
        }

        `in`.copyInto(buf, destinationOffset = bufOff, startIndex = inOffT, endIndex = inOffT + lenT)

        bufOff+= lenT

        return resultLen
    }

    override fun doFinal(out: ByteArray, outOff: Int): Int {
        val blockSize = cipher.getBlockSize()
        var resultLen = 0

        if (forEncryption) {
            if (bufOff == blockSize) {
                if ((outOff + 2 * blockSize) > out.size) {
                    reset()

                    throw OutputLengthException("output buffer too short")
                }

                resultLen = cipher.processBlock(buf, 0, out, outOff)
                bufOff = 0
            }

            padding.addPadding(buf, bufOff)

            resultLen += cipher.processBlock(buf, 0, out, outOff + resultLen)

            reset()
        } else {
            if (bufOff == blockSize) {
                resultLen = cipher.processBlock(buf, 0, buf, 0)
                bufOff = 0
            } else {
                reset()

                throw DataLengthException("last block incomplete in decryption")
            }

            try {
                resultLen -= padding.padCount(buf)

                buf.copyInto(out, destinationOffset = outOff, startIndex = 0, endIndex = resultLen)
            } finally {
                reset()
            }
        }

        return resultLen
    }
}