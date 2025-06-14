package id.feinn.utility.bytebuffer

import kotlin.math.min

/**
 * Abstract base class for byte buffer implementations.
 *
 * Provides core byte buffer operations including:
 * - Reading/writing primitive types (bytes, shorts, ints, longs, floats, doubles)
 * - Bulk put/get operations
 * - Byte order control (big/little endian)
 * - Buffer comparison
 *
 * Inherits from FeinnBuffer<ByteArray> and implements Comparable<FeinnByteBuffer>
 */
public abstract class FeinnByteBuffer : FeinnBuffer<ByteArray>, Comparable<FeinnByteBuffer> {

    /**
     * Companion object containing factory methods and utility functions.
     */
    public companion object {

        /**
         * Wraps a byte array into a new FeinnByteBuffer.
         *
         * @param array The byte array to wrap
         * @param offset The offset within the array
         * @param length The number of bytes to use
         * @return A new FeinnHeapByteBuffer instance
         * @throws IndexOutOfBoundsException If offset/length are invalid
         */
        public fun wrap(array: ByteArray, offset: Int, length: Int) : FeinnByteBuffer {
            return try {
                FeinnHeapByteBuffer(array, offset, length)
            } catch (e: IllegalArgumentException) {
                throw IndexOutOfBoundsException()
            }
        }

        /**
         * Wraps an entire byte array into a new FeinnByteBuffer.
         *
         * @param array The byte array to wrap
         * @return A new FeinnHeapByteBuffer instance
         */
        public fun wrap(array: ByteArray) : FeinnByteBuffer = wrap(array, 0, array.size)

        /**
         * Compares two bytes.
         *
         * @param x First byte to compare
         * @param y Second byte to compare
         * @return Comparison result (0 if equal, negative if x < y, positive if x > y)
         */
        private fun compare(x: Byte, y: Byte): Int {
            return x.compareTo(y)
        }

        /**
         * Allocates a new byte buffer with the given capacity.
         *
         * @param capacity The buffer capacity in bytes
         * @return A new FeinnHeapByteBuffer instance
         * @throws IllegalArgumentException If capacity is negative
         */
        public fun allocate(capacity: Int) : FeinnByteBuffer {
            if (capacity < 0)
                throw createCapacityException(capacity)
            return FeinnHeapByteBuffer(capacity, capacity)
        }

    }

    /**
     * The backing byte array (nullable).
     */
    protected var hb: ByteArray? = null

    /**
     * The offset within the backing array.
     */
    protected var offset: Int = 0

    /**
     * Whether the buffer is read-only.
     */
    protected abstract var isReadOnly: Boolean

    /**
     * Whether the buffer uses big-endian byte order (default: true).
     */
    protected var bigEndian: Boolean = true

    /**
     * Primary constructor for byte buffers.
     *
     * @param mark The mark position
     * @param pos The initial position
     * @param lim The initial limit
     * @param cap The capacity
     * @param hb The backing byte array
     * @param offset The offset within the array
     */
    public constructor(mark: Int, pos: Int, lim: Int, cap: Int, hb: ByteArray, offset: Int) : super(mark, pos, lim, cap) {
        this.hb = hb
        this.offset = offset
    }

    /**
     * Gets a single byte from the current position and increments position.
     *
     * @return The byte at current position
     */
    public abstract fun get() : Byte

    /**
     * Gets a single byte from the specified absolute index.
     *
     * @param index The absolute index
     * @return The byte at specified index
     */
    public abstract fun get(index: Int): Byte

    /**
     * Internal method for bulk get operations.
     */
    protected fun getArray(index: Int, dst: ByteArray, offset: Int, length: Int): FeinnByteBuffer {
        // Optional bounds check
        if (offset < 0 || length < 0 || offset + length > dst.size) {
            throw IndexOutOfBoundsException("Range [offset=$offset, length=$length) out of bounds for size ${dst.size}")
        }

        for (i in 0 until length) {
            dst[offset + i] = get(index + i)
        }

        return this
    }

    /**
     * Bulk get method that transfers bytes to the destination array.
     */
    public fun get(dst: ByteArray, offset: Int, length: Int): FeinnByteBuffer {
        ByteBufferUtils.checkFromIndexSize(offset, length, dst.size)
        val pos = position
        if (length > limit - pos)
            throw FeinnBufferUnderflowException()

        getArray(pos, dst, offset, length)
        position = pos + length
        return this
    }

    /**
     * Bulk get method that transfers bytes to the destination array.
     */
    public fun get(dst: ByteArray): FeinnByteBuffer {
        return get(dst, 0, dst.size)
    }

    /**
     * Bulk get method that transfers bytes to the destination array.
     */
    public fun get(index: Int, dst: ByteArray): FeinnByteBuffer {
        return get(index, dst, 0, dst.size)
    }

    /**
     * Bulk get method that transfers bytes to the destination array.
     */
    public fun get(index: Int, dst: ByteArray, offset: Int, length: Int): FeinnByteBuffer {
        ByteBufferUtils.checkFromIndexSize(index, length, limit)
        ByteBufferUtils.checkFromIndexSize(offset, length, dst.size)

        getArray(index, dst, offset, length)

        return this
    }

    /**
     * Compares this buffer to another buffer.
     *
     * @param other The buffer to compare with
     * @return Comparison result (0 if equal, negative if less, positive if greater)
     */
    public override fun compareTo(other: FeinnByteBuffer): Int {
        // FIXME: fix this not equals from nio
        val thisPos: Int = this.position
        val thisRem: Int = this.limit - thisPos
        val thatPos: Int = other.position
        val thatRem: Int = other.limit - thatPos
        val length = min(thisRem, thatRem)
        if (length < 0) return -1
        val i: Int = FeinnBufferMismatch.mismatch(
            this, thisPos,
            other, thatPos,
            length
        )
        println(i)
        if (i >= 0) {
            return compare(this.get(thisPos + i), other.get(thatPos + i))
        }
        return thisRem - thatRem
    }

    /**
     * Returns the backing byte array.
     *
     * @return The backing array
     * @throws UnsupportedOperationException If no backing array exists
     * @throws FeinnReadOnlyBufferException If buffer is read-only
     */
    public override fun array(): ByteArray {
        if (hb == null) throw UnsupportedOperationException()
        if (isReadOnly) throw FeinnReadOnlyBufferException()
        return hb!!
    }

    /**
     * Rewinds this buffer by resetting the position to 0 and discarding the mark.
     *
     * @return This buffer
     */
    public override fun rewind(): FeinnByteBuffer {
        super.rewind()
        return this
    }

    /**
     * Reads the next two bytes at current position as a Char and increments position by 2.
     *
     * @return The Char value read
     */
    public abstract fun getChar(): Char

    /**
     * Sets the byte order of this buffer.
     *
     * @param bo The new byte order (BIG_ENDIAN or LITTLE_ENDIAN)
     * @return This buffer
     */
    public fun order(bo: FeinnByteOrder): FeinnByteBuffer {
        bigEndian = (bo == FeinnByteOrder.BIG_ENDIAN)
        return this
    }

    /**
     * Gets the current byte order of this buffer.
     *
     * @return The current byte order
     */
    public fun order(): FeinnByteOrder {
        return if (bigEndian) FeinnByteOrder.BIG_ENDIAN else FeinnByteOrder.LITTLE_ENDIAN
    }

    /**
     * Reads two bytes from specified index as a Char without changing position.
     *
     * @param index The absolute index to read from
     * @return The Char value read
     */
    public abstract fun getChar(index: Int): Char

    /**
     * Reads the next two bytes at current position as a Short and increments position by 2.
     *
     * @return The Short value read
     */
    public abstract fun getShort(): Short

    /**
     * Reads two bytes from specified index as a Short without changing position.
     *
     * @param index The absolute index to read from
     * @return The Short value read
     */
    public abstract fun getShort(index: Int): Short

    /**
     * Reads the next eight bytes at current position as a Double and increments position by 8.
     *
     * @return The Double value read
     */
    public abstract fun getDouble(): Double

    /**
     * Reads eight bytes from specified index as a Double without changing position.
     *
     * @param index The absolute index to read from
     * @return The Double value read
     */
    public abstract fun getDouble(index: Int): Double

    /**
     * Reads the next four bytes at current position as an Int and increments position by 4.
     *
     * @return The Int value read
     */
    public abstract fun getInt(): Int

    /**
     * Reads four bytes from specified index as an Int without changing position.
     *
     * @param index The absolute index to read from
     * @return The Int value read
     */
    public abstract fun getInt(index: Int): Int

    /**
     * Reads the next eight bytes at current position as a Long and increments position by 8.
     *
     * @return The Long value read
     */
    public abstract fun getLong(): Long

    /**
     * Reads eight bytes from specified index as a Long without changing position.
     *
     * @param index The absolute index to read from
     * @return The Long value read
     */
    public abstract fun getLong(index: Int): Long

    /**
     * Reads the next four bytes at current position as a Float and increments position by 4.
     *
     * @return The Float value read
     */
    public abstract fun getFloat(): Float

    /**
     * Reads four bytes from specified index as a Float without changing position.
     *
     * @param index The absolute index to read from
     * @return The Float value read
     */
    public abstract fun getFloat(index: Int): Float

    /**
     * Writes a byte at current position and increments position by 1.
     *
     * @param b The byte to write
     * @return This buffer
     */
    public abstract fun put(b: Byte): FeinnByteBuffer

    /**
     * Writes a byte at specified absolute index without changing position.
     *
     * @param index The absolute index to write at
     * @param b The byte to write
     * @return This buffer
     */
    public abstract fun put(index: Int, b: Byte): FeinnByteBuffer

    /**
     * Transfers all remaining bytes from source buffer to this buffer.
     *
     * @param src The source buffer
     * @return This buffer
     * @throws IllegalArgumentException If source buffer is this buffer
     * @throws FeinnReadOnlyBufferException If this buffer is read-only
     * @throws FeinnBufferOverflowException If insufficient space remains
     */
    public fun put(src: FeinnByteBuffer): FeinnByteBuffer {
        if (src === this)
            throw createSameBufferException()
        if (isReadOnly)
            throw FeinnReadOnlyBufferException()

        val srcPos = src.position
        val srcLim = src.limit
        val srcRem = if (srcPos <= srcLim) srcLim - srcPos else 0
        val pos = position
        val lim = limit
        val rem = if (pos <= lim) lim - pos else 0

        if (srcRem > rem)
            throw FeinnBufferOverflowException()

        putBuffer(pos, src, srcPos, srcRem)

        position = pos + srcRem
        src.position = srcPos + srcRem

        return this
    }

    /**
     * Transfers bytes from source buffer to this buffer at specified position.
     *
     * @param index The starting position in this buffer
     * @param src The source buffer
     * @param offset The offset in source buffer
     * @param length The number of bytes to transfer
     * @return This buffer
     * @throws IndexOutOfBoundsException If ranges are invalid
     * @throws FeinnReadOnlyBufferException If this buffer is read-only
     */
    public open fun put(index: Int, src: FeinnByteBuffer, offset: Int, length: Int): FeinnByteBuffer {
        ByteBufferUtils.checkFromIndexSize(index, length, limit)
        ByteBufferUtils.checkFromIndexSize(offset, length, src.limit)
        if (isReadOnly)
            throw FeinnReadOnlyBufferException()

        putBuffer(index, src, offset, length)

        return this
    }

    /**
     * Internal method for transferring bytes between buffers.
     */
    private fun putBuffer(pos: Int, src: FeinnByteBuffer, srcPos: Int, n: Int) {
        // Validasi parameter supaya tidak out of bounds
        require(srcPos >= 0 && n >= 0 && srcPos + n <= src.limit) { "Invalid src range" }
        require(pos >= 0 && pos + n <= this.limit) { "Invalid dest range" }
        require(!isReadOnly) { "Buffer is read-only" }

        // Backup posisi asli
        val oldSrcPos = src.position
        val oldDestPos = this.position

        try {
            // Set posisi di src dan dest untuk copy
            src.position = srcPos
            this.position = pos

            // Buat temporary array untuk menampung data
            val temp = ByteArray(n)
            src.get(temp)           // Baca n byte dari src
            this.put(temp)          // Tulis ke buffer tujuan

        } finally {
            // Kembalikan posisi buffer ke kondisi semula
            src.position = oldSrcPos
            this.position = oldDestPos
        }
    }

    /**
     * Transfers bytes from array to this buffer at current position.
     *
     * @param src The source array
     * @param offset The offset in source array
     * @param length The number of bytes to transfer
     * @return This buffer
     * @throws IndexOutOfBoundsException If ranges are invalid
     * @throws FeinnReadOnlyBufferException If this buffer is read-only
     * @throws FeinnBufferOverflowException If insufficient space remains
     */
    public open fun put(src: ByteArray, offset: Int, length: Int): FeinnByteBuffer {
        if (isReadOnly)
            throw FeinnReadOnlyBufferException()
        ByteBufferUtils.checkFromIndexSize(offset, length, src.size)

        val pos = position
        if (length > limit - pos)
            throw FeinnBufferOverflowException()

        putArray(pos, src, offset, length)

        position = pos + length
        return this
    }

    /**
     * Transfers entire array contents to this buffer at current position.
     *
     * @param src The source array
     * @return This buffer
     */
    public open fun put(src: ByteArray): FeinnByteBuffer = put(src, 0, src.size)

    /**
     * Transfers bytes from array to this buffer at specified position.
     *
     * @param index The starting position in this buffer
     * @param src The source array
     * @param offset The offset in source array
     * @param length The number of bytes to transfer
     * @return This buffer
     * @throws IndexOutOfBoundsException If ranges are invalid
     * @throws FeinnReadOnlyBufferException If this buffer is read-only
     */
    public open fun put(index: Int, src: ByteArray, offset: Int, length: Int): FeinnByteBuffer {
        if (isReadOnly)
            throw FeinnReadOnlyBufferException()

        ByteBufferUtils.checkFromIndexSize(index, length, limit)
        ByteBufferUtils.checkFromIndexSize(offset, length, src.size)

        putArray(index, src, offset, length)

        return this
    }

    /**
     * Transfers entire array contents to this buffer at specified position.
     *
     * @param index The starting position in this buffer
     * @param src The source array
     * @return This buffer
     */
    public open fun put(index: Int, src: ByteArray): FeinnByteBuffer = put(index, src, 0, src.size)

    /**
     * Internal method for transferring bytes from array to buffer.
     */
    private fun putArray(index: Int, src: ByteArray, offset: Int, length: Int): FeinnByteBuffer {
        // Copy manual byte per byte
        val end = offset + length
        var j = index
        for (i in offset until end) {
            this.put(j, src[i])
            j++
        }
        return this
    }

    /**
     * Writes two bytes containing Char value at current position and increments position by 2.
     *
     * @param value The Char value to write
     * @return This buffer
     */
    public abstract fun putChar(value: Char): FeinnByteBuffer

    /**
     * Writes two bytes containing Char value at specified absolute index.
     *
     * @param index The absolute index to write at
     * @param value The Char value to write
     * @return This buffer
     */
    public abstract fun putChar(index: Int, value: Char): FeinnByteBuffer

    /**
     * Writes two bytes containing Short value at current position and increments position by 2.
     *
     * @param value The Short value to write
     * @return This buffer
     */
    public abstract fun putShort(value: Short): FeinnByteBuffer

    /**
     * Writes two bytes containing Short value at specified absolute index.
     *
     * @param index The absolute index to write at
     * @param value The Short value to write
     * @return This buffer
     */
    public abstract fun putShort(index: Int, value: Short): FeinnByteBuffer

    /**
     * Writes four bytes containing Int value at current position and increments position by 4.
     *
     * @param value The Int value to write
     * @return This buffer
     */
    public abstract fun putInt(value: Int): FeinnByteBuffer

    /**
     * Writes four bytes containing Int value at specified absolute index.
     *
     * @param index The absolute index to write at
     * @param value The Int value to write
     * @return This buffer
     */
    public abstract fun putInt(index: Int, value: Int): FeinnByteBuffer

    /**
     * Writes eight bytes containing Long value at current position and increments position by 8.
     *
     * @param value The Long value to write
     * @return This buffer
     */
    public abstract fun putLong(value: Long): FeinnByteBuffer

    /**
     * Writes eight bytes containing Long value at specified absolute index.
     *
     * @param index The absolute index to write at
     * @param value The Long value to write
     * @return This buffer
     */
    public abstract fun putLong(index: Int, value: Long): FeinnByteBuffer

    /**
     * Writes four bytes containing Float value at current position and increments position by 4.
     *
     * @param value The Float value to write
     * @return This buffer
     */
    public abstract fun putFloat(value: Float): FeinnByteBuffer

    /**
     * Writes four bytes containing Float value at specified absolute index.
     *
     * @param index The absolute index to write at
     * @param value The Float value to write
     * @return This buffer
     */
    public abstract fun putFloat(index: Int, value: Float): FeinnByteBuffer

    /**
     * Writes eight bytes containing Double value at current position and increments position by 8.
     *
     * @param value The Double value to write
     * @return This buffer
     */
    public abstract fun putDouble(value: Double): FeinnByteBuffer

    /**
     * Writes eight bytes containing Double value at specified absolute index.
     *
     * @param index The absolute index to write at
     * @param value The Double value to write
     * @return This buffer
     */
    public abstract fun putDouble(index: Int, value: Double): FeinnByteBuffer


}