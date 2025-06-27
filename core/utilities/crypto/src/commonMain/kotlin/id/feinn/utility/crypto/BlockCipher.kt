package id.feinn.utility.crypto

public interface BlockCipher {

    @Throws(IllegalArgumentException::class)
    public fun init(forEncryption: Boolean, params: CipherParameters)

    public fun getAlgorithmName(): String

    public fun getBlockSize(): Int

    @Throws(OutputLengthException::class, IllegalStateException::class)
    public fun processBlock(input: ByteArray, inOff: Int, output: ByteArray, outOff: Int): Int

    public fun reset()

}