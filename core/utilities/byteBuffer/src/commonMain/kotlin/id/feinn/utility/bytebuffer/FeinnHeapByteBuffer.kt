package id.feinn.utility.bytebuffer

internal class FeinnHeapByteBuffer : FeinnByteBuffer {

    constructor(buf: ByteArray, off: Int, len: Int) : super(-1, off, off + len, buf.size, buf, 0)

    protected override var isReadOnly: Boolean
        get() = false
        set(value) { }

    override fun get(): Byte = hb!![ix(nextGetIndex())]

    override fun get(i: Int): Byte = hb!![ix(checkIndex(i))]

    override fun getChar(): Char {
        val index = nextGetIndex(2)
        val high = hb!![index]
        val low = hb!![index + 1]
        return KotlinUtils.toChar(high, low, bigEndian)
    }

    override fun getChar(i: Int): Char {
        val index = checkIndex(i, 2)
        val high = hb!![index]
        val low = hb!![index + 1]
        return KotlinUtils.toChar(high, low, bigEndian)

    }

    override fun getShort(): Short {
        val index = nextGetIndex(2)
        val high = hb!![index]
        val low = hb!![index + 1]
        return KotlinUtils.toShort(high, low, bigEndian)
    }

    override fun getShort(i: Int): Short {
        val index = checkIndex(i, 2)
        val high = hb!![index]
        val low = hb!![index + 1]
        return KotlinUtils.toShort(high, low, bigEndian)
    }

    override fun getDouble(): Double {
        val index = nextGetIndex(8)
        val bytes = ByteArray(8) { hb!![index + it] }
        return KotlinUtils.toDouble(bytes, bigEndian)
    }

    override fun getDouble(i: Int): Double {
        val index = checkIndex(i, 8)
        val bytes = ByteArray(8) { hb!![index + it] }
        return KotlinUtils.toDouble(bytes, bigEndian)
    }

    override fun getInt(): Int {
        val index = nextGetIndex(4)
        val bytes = ByteArray(4) { hb!![index + it] }
        return KotlinUtils.toInt(bytes, bigEndian)
    }

    override fun getInt(i: Int): Int {
        val index = checkIndex(i, 4)
        val bytes = ByteArray(4) { hb!![index + it] }
        return KotlinUtils.toInt(bytes, bigEndian)
    }

    override fun getLong(): Long {
        val index = nextGetIndex(8)
        val bytes = ByteArray(8) { hb!![index + it] }
        return KotlinUtils.toLong(bytes, bigEndian)
    }

    override fun getLong(i: Int): Long {
        val index = checkIndex(i, 8)
        val bytes = ByteArray(8) { hb!![index + it] }
        return KotlinUtils.toLong(bytes, bigEndian)
    }

    override fun getFloat(): Float {
        val index = nextGetIndex(4)
        val bytes = ByteArray(4) { hb!![index + it] }
        return KotlinUtils.toFloat(bytes, bigEndian)
    }

    override fun getFloat(i: Int): Float {
        val index = checkIndex(i, 4)
        val bytes = ByteArray(4) { hb!![index + it] }
        return KotlinUtils.toFloat(bytes, bigEndian)
    }

    override fun put(x: Byte): FeinnByteBuffer {
        hb!![ix(nextPutIndex())] = x
        return this
    }

    override fun put(i: Int, x: Byte): FeinnByteBuffer {
        hb!![ix(checkIndex(i))] = x
        return this
    }

    override fun put(src: ByteArray, offset: Int, length: Int): FeinnByteBuffer {
        KotlinUtils.checkFromIndexSize(offset, length, src.size)

        val pos = position
        if (length > limit - pos) {
            throw FeinnBufferOverflowException()
        }

        // Salin array ke backing array `hb` mulai dari posisi `ix(pos)`
        src.copyInto(hb!!, destinationOffset = ix(pos), startIndex = offset, endIndex = offset + length)

        position = pos + length
        return this
    }

    override fun put(src: ByteArray): FeinnByteBuffer {
        super.put(src)
        return this
    }

    override fun put(index: Int, src: FeinnByteBuffer, offset: Int, length: Int): FeinnByteBuffer {
        super.put(index, src, offset, length)
        return this
    }

    override fun put(index: Int, src: ByteArray, offset: Int, length: Int): FeinnByteBuffer {
        KotlinUtils.checkFromIndexSize(index, length, limit)
        KotlinUtils.checkFromIndexSize(offset, length, src.size)
        src.copyInto(hb!!, destinationOffset = ix(index), startIndex = offset, endIndex = offset + length)
        return this
    }

    override fun putChar(x: Char): FeinnByteBuffer {
        val index = nextPutIndex(2)
        val bytes = KotlinUtils.toByteArray(x, bigEndian)
        for (i in bytes.indices) {
            hb!![index + i] = bytes[i]
        }
        return this
    }

    override fun putChar(i: Int, x: Char): FeinnByteBuffer {
        val index = checkIndex(i, 2)
        val bytes = KotlinUtils.toByteArray(x, bigEndian)
        for (j in bytes.indices) {
            hb!![index + j] = bytes[j]
        }
        return this
    }

    override fun putShort(x: Short): FeinnByteBuffer {
        val index = nextPutIndex(2)
        val bytes = KotlinUtils.toByteArray(x, bigEndian)
        for ((i, b) in bytes.withIndex()) {
            hb!![index + i] = b
        }
        return this
    }

    override fun putShort(i: Int, x: Short): FeinnByteBuffer {
        val index = checkIndex(i, 2)
        val bytes = KotlinUtils.toByteArray(x, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putInt(x: Int): FeinnByteBuffer {
        val index = nextPutIndex(4)
        val bytes = KotlinUtils.toByteArray(x, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putInt(i: Int, x: Int): FeinnByteBuffer {
        val index = checkIndex(i, 4)
        val bytes = KotlinUtils.toByteArray(x, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putLong(x: Long): FeinnByteBuffer {
        val index = nextPutIndex(8)
        val bytes = KotlinUtils.toByteArray(x, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putLong(i: Int, x: Long): FeinnByteBuffer {
        val index = checkIndex(i, 8)
        val bytes = KotlinUtils.toByteArray(x, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putFloat(x: Float): FeinnByteBuffer {
        val y = x.toRawBits()
        val index = nextPutIndex(4)
        val bytes = KotlinUtils.toByteArray(y, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putFloat(i: Int, x: Float): FeinnByteBuffer {
        val y = x.toRawBits()
        val index = checkIndex(i, 4)
        val bytes = KotlinUtils.toByteArray(y, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putDouble(x: Double): FeinnByteBuffer {
        val y = x.toRawBits()
        val index = nextPutIndex(8)
        val bytes = KotlinUtils.toByteArray(y, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putDouble(i: Int, x: Double): FeinnByteBuffer {
        val y = x.toRawBits()
        val index = checkIndex(i, 8)
        val bytes = KotlinUtils.toByteArray(y, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    protected fun ix(i: Int) : Int = i + offset

}