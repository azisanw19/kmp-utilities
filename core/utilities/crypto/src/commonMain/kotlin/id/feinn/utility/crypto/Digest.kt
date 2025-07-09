package id.feinn.utility.crypto

public interface Digest {

    public fun getAlgorithmName(): String

    public fun getDigestSize(): Int

    public fun update(`in`: Byte)

    public fun update(`in`: ByteArray, inOff: Int, len: Int)

    public fun doFinal(out: ByteArray, outOff: Int): Int

    public fun reset()

}