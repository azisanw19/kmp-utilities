package id.feinn.utility.crypto

import kotlin.jvm.Throws

abstract class DefaultMultiBlockCipher protected constructor(): MultiBlockCipher {

    override fun getMultiBlockSize(): Int = this.getBlockSize()

    @Throws(DataLengthException::class, IllegalStateException::class)
    override fun processBlocks(`in`: ByteArray, inOff: Int, blockCount: Int, out: ByteArray, outOff: Int): Int {
        var inOffT = inOff

        var resultLen = 0
        val blockSize = this.getMultiBlockSize()

        var i = 0
        while (i != blockCount) {

            resultLen += this.processBlock(`in`, inOffT, out, outOff + resultLen)

            inOffT += blockSize

            i++
        }

        return resultLen
    }

}