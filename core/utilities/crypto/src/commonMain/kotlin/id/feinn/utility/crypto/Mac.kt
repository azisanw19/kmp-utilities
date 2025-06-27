package id.feinn.utility.crypto

public interface Mac {

    @Throws(IllegalArgumentException::class)
    public fun init(params: CipherParameters)

    public fun getAlgorithmName(): String

    public fun getMacSize(): Int

    @Throws(IllegalStateException::class)
    public fun update(`in`: Byte)

    @Throws(DataLengthException::class, IllegalStateException::class)
    public fun update(`in`: ByteArray, inOff: Int, len: Int)

    @Throws(DataLengthException::class, IllegalStateException::class)
    public fun doFinal(out: ByteArray, outOff: Int): Int

    public fun reset()

}