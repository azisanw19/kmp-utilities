package id.feinn.utility.bytebuffer

import kotlin.math.min

public abstract class FeinnByteBuffer : FeinnBuffer<ByteArray>, Comparable<FeinnByteBuffer> {

    public companion object {

        public fun wrap(array: ByteArray, offset: Int, length: Int) : FeinnByteBuffer {
            return try {
                FeinnHeapByteBuffer(array, offset, length)
            } catch (e: IllegalArgumentException) {
                throw IndexOutOfBoundsException()
            }
        }

        public fun wrap(array: ByteArray) : FeinnByteBuffer = wrap(array, 0, array.size)

        private fun compare(x: Byte, y: Byte): Int {
            return x.compareTo(y)
        }

    }

    protected var hb: ByteArray? = null
    protected var offset: Int = 0
    protected abstract var isReadOnly: Boolean

    protected var bigEndian: Boolean = true

    public constructor(mark: Int, pos: Int, lim: Int, cap: Int, hb: ByteArray, offset: Int) : super(mark, pos, lim, cap) {
        this.hb = hb
        this.offset = offset
    }

    public abstract fun get() : Byte

    public abstract fun get(index: Int): Byte

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

    public fun get(dst: ByteArray, offset: Int, length: Int): FeinnByteBuffer {
        KotlinUtils.checkFromIndexSize(offset, length, dst.size)
        val pos = position
        if (length > limit - pos)
            throw FeinnBufferUnderflowException()

        getArray(pos, dst, offset, length)
        position = pos + length
        return this
    }

    public fun get(dst: ByteArray): FeinnByteBuffer {
        return get(dst, 0, dst.size)
    }

    public fun get(index: Int, dst: ByteArray): FeinnByteBuffer {
        return get(index, dst, 0, dst.size)
    }

    public fun get(index: Int, dst: ByteArray, offset: Int, length: Int): FeinnByteBuffer {
        KotlinUtils.checkFromIndexSize(index, length, limit)
        KotlinUtils.checkFromIndexSize(offset, length, dst.size)

        getArray(index, dst, offset, length)

        return this
    }

    public override fun compareTo(other: FeinnByteBuffer): Int {
        // TODO fix this not equals from nio
        val thisPos: Int = this.position
        val thisRem: Int = this.limit - thisPos
        val thatPos: Int = other.position
        val thatRem: Int = other.limit - thatPos
        val length = min(thisRem, thatRem)
        if (length < 0) return -1
        val i: Int = BufferMismatch.mismatch(
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

    public override fun array(): ByteArray {
        if (hb == null) throw UnsupportedOperationException()
        if (isReadOnly) throw FeinnReadOnlyBufferException()
        return hb!!
    }

    public override fun rewind(): FeinnByteBuffer {
        super.rewind()
        return this
    }

    public abstract fun getChar(): Char

    public fun order(bo: FeinnByteOrder): FeinnByteBuffer {
        bigEndian = (bo == FeinnByteOrder.BIG_ENDIAN)
        return this
    }

    public fun order(): FeinnByteOrder {
        return if (bigEndian) FeinnByteOrder.BIG_ENDIAN else FeinnByteOrder.LITTLE_ENDIAN
    }

    public abstract fun getChar(index: Int): Char

    public abstract fun getShort(): Short

    public abstract fun getShort(index: Int): Short

    public abstract fun getDouble(): Double

    public abstract fun getDouble(index: Int): Double

    public abstract fun getInt(): Int

    public abstract fun getInt(index: Int): Int

    public abstract fun getLong(): Long

    public abstract fun getLong(index: Int): Long

    public abstract fun getFloat(): Float

    public abstract fun getFloat(index: Int): Float

    public abstract fun put(b: Byte): FeinnByteBuffer

    public abstract fun put(index: Int, b: Byte): FeinnByteBuffer

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

    public open fun put(index: Int, src: FeinnByteBuffer, offset: Int, length: Int): FeinnByteBuffer {
        KotlinUtils.checkFromIndexSize(index, length, limit)
        KotlinUtils.checkFromIndexSize(offset, length, src.limit)
        if (isReadOnly)
            throw FeinnReadOnlyBufferException()

        putBuffer(index, src, offset, length)

        return this
    }

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

    public open fun put(src: ByteArray, offset: Int, length: Int): FeinnByteBuffer {
        if (isReadOnly)
            throw FeinnReadOnlyBufferException()
        KotlinUtils.checkFromIndexSize(offset, length, src.size)

        val pos = position
        if (length > limit - pos)
            throw FeinnBufferOverflowException()

        putArray(pos, src, offset, length)

        position = pos + length
        return this
    }

    public open fun put(src: ByteArray): FeinnByteBuffer = put(src, 0, src.size)

    public open fun put(index: Int, src: ByteArray, offset: Int, length: Int): FeinnByteBuffer {
        if (isReadOnly)
            throw FeinnReadOnlyBufferException()

        KotlinUtils.checkFromIndexSize(index, length, limit)
        KotlinUtils.checkFromIndexSize(offset, length, src.size)

        putArray(index, src, offset, length)

        return this
    }

    public open fun put(index: Int, src: ByteArray): FeinnByteBuffer = put(index, src, 0, src.size)

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

    public abstract fun putChar(value: Char): FeinnByteBuffer

    public abstract fun putChar(index: Int, value: Char): FeinnByteBuffer

    public abstract fun putShort(value: Short): FeinnByteBuffer

    public abstract fun putShort(index: Int, value: Short): FeinnByteBuffer

    public abstract fun putInt(value: Int): FeinnByteBuffer

    public abstract fun putInt(index: Int, value: Int): FeinnByteBuffer

    public abstract fun putLong(value: Long): FeinnByteBuffer

    public abstract fun putLong(index: Int, value: Long): FeinnByteBuffer

    public abstract fun putFloat(value: Float): FeinnByteBuffer

    public abstract fun putFloat(index: Int, value: Float): FeinnByteBuffer

    public abstract fun putDouble(value: Double): FeinnByteBuffer

    public abstract fun putDouble(index: Int, value: Double): FeinnByteBuffer


}