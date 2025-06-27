package id.feinn.utility.crypto

public abstract class StreamBlockCipher protected constructor(private val cipher: BlockCipher) : DefaultMultiBlockCipher(), StreamCipher {

    public fun getUnderlyingCipher(): BlockCipher = cipher

    override fun returnByte(`in`: Byte): Byte = calculateByte(`in`)

    @Throws(DataLengthException::class)
    override fun processBytes(`in`: ByteArray, inOff: Int, len: Int, out: ByteArray, outOff: Int): Int {
        if (inOff + len > `in`.size) throw DataLengthException("input buffer too small")
        if (outOff + len > out.size) throw OutputLengthException("output buffet too short")

        var inStart = inOff
        val inEnd = inOff + len
        var outStart = outOff

        while (inStart < inEnd) out[outStart++] = calculateByte(`in`[inStart++])

        return len
    }

    protected abstract fun calculateByte(b: Byte): Byte

}