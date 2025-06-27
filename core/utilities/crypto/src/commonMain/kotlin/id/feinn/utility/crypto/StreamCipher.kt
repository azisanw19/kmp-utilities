package id.feinn.utility.crypto

public interface StreamCipher {

    @Throws(IllegalArgumentException::class)
    public fun init(forEncryption: Boolean, params: CipherParameters)

    public fun getAlgorithmName(): String

    public fun returnByte(`in`: Byte): Byte

    @Throws(DataLengthException::class)
    public fun processBytes(`in`: ByteArray, inOff: Int, len: Int, out: ByteArray, outOff: Int): Int

    public fun reset()

}