package id.feinn.utility.crypto

public interface MultiBlockCipher : BlockCipher {

    public fun getMultiBlockSize(): Int

    @Throws(DataLengthException::class, IllegalStateException::class)
    public fun processBlocks(`in`: ByteArray, inOff: Int, blockCount: Int, out: ByteArray, outOff: Int): Int

}