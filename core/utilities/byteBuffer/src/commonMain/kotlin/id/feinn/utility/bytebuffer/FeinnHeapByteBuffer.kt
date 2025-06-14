package id.feinn.utility.bytebuffer

/**
 * A heap-based implementation of [FeinnByteBuffer] that uses a byte array as its backing storage.
 *
 * This class provides the concrete implementation for all abstract methods in [FeinnByteBuffer],
 * handling the actual storage and retrieval of bytes in heap memory. It supports both big-endian
 * and little-endian byte ordering for multibyte operations.
 *
 * @constructor Creates a buffer backed by the specified byte array
 * @param buf The backing byte array
 * @param off The offset within the array
 * @param len The number of bytes to use
 */
internal class FeinnHeapByteBuffer : FeinnByteBuffer {

    /**
     * Creates a buffer backed by the specified byte array range.
     */
    constructor(buf: ByteArray, off: Int, len: Int) : super(-1, off, off + len, buf.size, buf, 0)

    /**
     * Creates a buffer with the specified capacity and limit.
     *
     * @param cap The buffer capacity
     * @param lim The initial limit
     */
    constructor(cap: Int, lim: Int) : super(-1, 0, lim, cap, byteArrayOf(), 0)

    /**
     * Indicates whether this buffer is read-only (always false for heap buffers).
     */
    override var isReadOnly: Boolean = false
        get() = false

    /**
     * Gets a byte from the current position and increments the position.
     *
     * @return The byte at current position
     * @throws FeinnBufferUnderflowException If position exceeds limit
     */
    override fun get(): Byte = hb!![ix(nextGetIndex())]

    /**
     * Gets a byte from the specified absolute index.
     *
     * @param i The absolute index
     * @return The byte at specified index
     * @throws IndexOutOfBoundsException If index is invalid
     */
    override fun get(i: Int): Byte = hb!![ix(checkIndex(i))]

    /**
     * Gets a Char from current position and increments position by 2.
     *
     * @return The Char value read
     * @throws FeinnBufferUnderflowException If remaining bytes < 2
     */
    override fun getChar(): Char {
        val index = nextGetIndex(2)
        val high = hb!![index]
        val low = hb!![index + 1]
        return ByteBufferUtils.toChar(high, low, bigEndian)
    }

    /**
     * Gets a Char from specified absolute index.
     *
     * @param i The absolute index
     * @return The Char value read
     * @throws IndexOutOfBoundsException If index is invalid or would exceed bounds
     */
    override fun getChar(i: Int): Char {
        val index = checkIndex(i, 2)
        val high = hb!![index]
        val low = hb!![index + 1]
        return ByteBufferUtils.toChar(high, low, bigEndian)

    }

    override fun getShort(): Short {
        val index = nextGetIndex(2)
        val high = hb!![index]
        val low = hb!![index + 1]
        return ByteBufferUtils.toShort(high, low, bigEndian)
    }

    override fun getShort(i: Int): Short {
        val index = checkIndex(i, 2)
        val high = hb!![index]
        val low = hb!![index + 1]
        return ByteBufferUtils.toShort(high, low, bigEndian)
    }

    override fun getDouble(): Double {
        val index = nextGetIndex(8)
        val bytes = ByteArray(8) { hb!![index + it] }
        return ByteBufferUtils.toDouble(bytes, bigEndian)
    }

    override fun getDouble(i: Int): Double {
        val index = checkIndex(i, 8)
        val bytes = ByteArray(8) { hb!![index + it] }
        return ByteBufferUtils.toDouble(bytes, bigEndian)
    }

    override fun getInt(): Int {
        val index = nextGetIndex(4)
        val bytes = ByteArray(4) { hb!![index + it] }
        return ByteBufferUtils.toInt(bytes, bigEndian)
    }

    override fun getInt(i: Int): Int {
        val index = checkIndex(i, 4)
        val bytes = ByteArray(4) { hb!![index + it] }
        return ByteBufferUtils.toInt(bytes, bigEndian)
    }

    override fun getLong(): Long {
        val index = nextGetIndex(8)
        val bytes = ByteArray(8) { hb!![index + it] }
        return ByteBufferUtils.toLong(bytes, bigEndian)
    }

    override fun getLong(i: Int): Long {
        val index = checkIndex(i, 8)
        val bytes = ByteArray(8) { hb!![index + it] }
        return ByteBufferUtils.toLong(bytes, bigEndian)
    }

    override fun getFloat(): Float {
        val index = nextGetIndex(4)
        val bytes = ByteArray(4) { hb!![index + it] }
        return ByteBufferUtils.toFloat(bytes, bigEndian)
    }

    override fun getFloat(i: Int): Float {
        val index = checkIndex(i, 4)
        val bytes = ByteArray(4) { hb!![index + it] }
        return ByteBufferUtils.toFloat(bytes, bigEndian)
    }

    /**
     * Puts a byte at current position and increments position.
     *
     * @param x The byte to write
     * @return This buffer
     * @throws FeinnBufferOverflowException If position exceeds limit
     */
    override fun put(x: Byte): FeinnByteBuffer {
        hb!![ix(nextPutIndex())] = x
        return this
    }

    /**
     * Puts a byte at specified absolute index.
     *
     * @param i The absolute index
     * @param x The byte to write
     * @return This buffer
     * @throws IndexOutOfBoundsException If index is invalid
     */
    override fun put(i: Int, x: Byte): FeinnByteBuffer {
        hb!![ix(checkIndex(i))] = x
        return this
    }

    /**
     * Transfers bytes from array to this buffer at current position.
     *
     * @param src The source array
     * @param offset The offset in source array
     * @param length The number of bytes to transfer
     * @return This buffer
     * @throws IndexOutOfBoundsException If ranges are invalid
     * @throws FeinnBufferOverflowException If insufficient space remains
     */
    override fun put(src: ByteArray, offset: Int, length: Int): FeinnByteBuffer {
        ByteBufferUtils.checkFromIndexSize(offset, length, src.size)

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
        ByteBufferUtils.checkFromIndexSize(index, length, limit)
        ByteBufferUtils.checkFromIndexSize(offset, length, src.size)
        src.copyInto(hb!!, destinationOffset = ix(index), startIndex = offset, endIndex = offset + length)
        return this
    }

    override fun putChar(x: Char): FeinnByteBuffer {
        val index = nextPutIndex(2)
        val bytes = ByteBufferUtils.toByteArray(x, bigEndian)
        for (i in bytes.indices) {
            hb!![index + i] = bytes[i]
        }
        return this
    }

    override fun putChar(i: Int, x: Char): FeinnByteBuffer {
        val index = checkIndex(i, 2)
        val bytes = ByteBufferUtils.toByteArray(x, bigEndian)
        for (j in bytes.indices) {
            hb!![index + j] = bytes[j]
        }
        return this
    }

    override fun putShort(x: Short): FeinnByteBuffer {
        val index = nextPutIndex(2)
        val bytes = ByteBufferUtils.toByteArray(x, bigEndian)
        for ((i, b) in bytes.withIndex()) {
            hb!![index + i] = b
        }
        return this
    }

    override fun putShort(i: Int, x: Short): FeinnByteBuffer {
        val index = checkIndex(i, 2)
        val bytes = ByteBufferUtils.toByteArray(x, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putInt(x: Int): FeinnByteBuffer {
        val index = nextPutIndex(4)
        val bytes = ByteBufferUtils.toByteArray(x, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putInt(i: Int, x: Int): FeinnByteBuffer {
        val index = checkIndex(i, 4)
        val bytes = ByteBufferUtils.toByteArray(x, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putLong(x: Long): FeinnByteBuffer {
        val index = nextPutIndex(8)
        val bytes = ByteBufferUtils.toByteArray(x, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putLong(i: Int, x: Long): FeinnByteBuffer {
        val index = checkIndex(i, 8)
        val bytes = ByteBufferUtils.toByteArray(x, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putFloat(x: Float): FeinnByteBuffer {
        val y = x.toRawBits()
        val index = nextPutIndex(4)
        val bytes = ByteBufferUtils.toByteArray(y, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putFloat(i: Int, x: Float): FeinnByteBuffer {
        val y = x.toRawBits()
        val index = checkIndex(i, 4)
        val bytes = ByteBufferUtils.toByteArray(y, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putDouble(x: Double): FeinnByteBuffer {
        val y = x.toRawBits()
        val index = nextPutIndex(8)
        val bytes = ByteBufferUtils.toByteArray(y, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    override fun putDouble(i: Int, x: Double): FeinnByteBuffer {
        val y = x.toRawBits()
        val index = checkIndex(i, 8)
        val bytes = ByteBufferUtils.toByteArray(y, bigEndian)
        for ((j, b) in bytes.withIndex()) {
            hb!![index + j] = b
        }
        return this
    }

    /**
     * Calculates the actual index in the backing array.
     *
     * @param i The logical buffer index
     * @return The physical array index
     */
    protected fun ix(i: Int) : Int = i + offset

}