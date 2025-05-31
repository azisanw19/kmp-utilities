package id.feinn.utility.bytebuffer

internal object KotlinUtils {

    fun checkFromIndexSize(offset: Int, length: Int, size: Int) {
        if (offset < 0 || length < 0 || offset + length > size) {
            throw IndexOutOfBoundsException("Range [offset=$offset, length=$length) out of bounds for size $size")
        }
    }

    fun toChar(high: Byte, low: Byte, bigEndian: Boolean = true): Char {
        return combineBytes(high, low, bigEndian).toChar()
    }

    fun toShort(high: Byte, low: Byte, bigEndian: Boolean = true): Short = combineBytes(high, low, bigEndian).toShort()

    private fun combineBytes(high: Byte, low: Byte, bigEndian: Boolean = true): Int {
        val b1 = high.toInt() and 0xFF
        val b2 = low.toInt() and 0xFF
        val value = if (bigEndian) {
            (b1 shl 8) or b2
        } else {
            (b2 shl 8) or b1
        }
        return value
    }

    fun toDouble(bytes: ByteArray, bigEndian: Boolean = true): Double {
        require(bytes.size == 8) { "Expected 8 bytes for double conversion" }
        val orderedBytes = if (bigEndian) bytes else bytes.reversedArray()
        var result = 0L
        for (b in orderedBytes) {
            result = (result shl 8) or (b.toLong() and 0xFF)
        }
        return Double.fromBits(result)
    }

    fun toInt(bytes: ByteArray, bigEndian: Boolean = true): Int {
        require(bytes.size == 4) { "Expected 4 bytes for Int conversion" }
        val ordered = if (bigEndian) bytes else bytes.reversedArray()
        var result = 0
        for (b in ordered) {
            result = (result shl 8) or (b.toInt() and 0xFF)
        }
        return result
    }

    fun toLong(bytes: ByteArray, bigEndian: Boolean = true): Long {
        require(bytes.size == 8) { "Expected 8 bytes for Long conversion" }
        val ordered = if (bigEndian) bytes else bytes.reversedArray()
        var result = 0L
        for (b in ordered) {
            result = (result shl 8) or (b.toLong() and 0xFF)
        }
        return result
    }

    fun toFloat(bytes: ByteArray, bigEndian: Boolean = true): Float {
        require(bytes.size == 4) { "Expected 4 bytes for Float conversion" }
        val ordered = if (bigEndian) bytes else bytes.reversedArray()
        var intBits = 0
        for (b in ordered) {
            intBits = (intBits shl 8) or (b.toInt() and 0xFF)
        }
        return Float.fromBits(intBits)
    }

    fun toByteArray(value: Char, bigEndian: Boolean): ByteArray {
        val v = value.code
        return if (bigEndian) {
            byteArrayOf((v shr 8).toByte(), v.toByte())
        } else {
            byteArrayOf(v.toByte(), (v shr 8).toByte())
        }
    }

    fun toByteArray(value: Short, bigEndian: Boolean): ByteArray {
        val intVal = value.toInt()
        return if (bigEndian) {
            byteArrayOf(
                (intVal shr 8).toByte(),
                intVal.toByte()
            )
        } else {
            byteArrayOf(
                intVal.toByte(),
                (intVal shr 8).toByte()
            )
        }
    }

    fun toByteArray(value: Int, bigEndian: Boolean): ByteArray {
        return if (bigEndian) {
            byteArrayOf(
                (value shr 24).toByte(),
                (value shr 16).toByte(),
                (value shr 8).toByte(),
                value.toByte()
            )
        } else {
            byteArrayOf(
                value.toByte(),
                (value shr 8).toByte(),
                (value shr 16).toByte(),
                (value shr 24).toByte()
            )
        }
    }

    fun toByteArray(value: Long, bigEndian: Boolean): ByteArray {
        return if (bigEndian) {
            byteArrayOf(
                (value shr 56).toByte(),
                (value shr 48).toByte(),
                (value shr 40).toByte(),
                (value shr 32).toByte(),
                (value shr 24).toByte(),
                (value shr 16).toByte(),
                (value shr 8).toByte(),
                value.toByte()
            )
        } else {
            byteArrayOf(
                value.toByte(),
                (value shr 8).toByte(),
                (value shr 16).toByte(),
                (value shr 24).toByte(),
                (value shr 32).toByte(),
                (value shr 40).toByte(),
                (value shr 48).toByte(),
                (value shr 56).toByte()
            )
        }
    }

}