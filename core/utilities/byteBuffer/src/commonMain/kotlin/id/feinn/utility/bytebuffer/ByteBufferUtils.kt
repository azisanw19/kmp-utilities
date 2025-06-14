package id.feinn.utility.bytebuffer

/**
 * Internal utility object for common ByteBuffer operations.
 */
internal object ByteBufferUtils {

    /**
     * Checks if the specified range [offset, offset + length) is within the bounds of [size].
     *
     * @param offset the starting index of the range
     * @param length the length of the range
     * @param size the size of the collection/array
     * @throws IndexOutOfBoundsException if the range is out of bounds
     */
    fun checkFromIndexSize(offset: Int, length: Int, size: Int) {
        if (offset < 0 || length < 0 || offset + length > size) {
            throw IndexOutOfBoundsException("Range [offset=$offset, length=$length) out of bounds for size $size")
        }
    }

    /**
     * Combines two bytes into a Char, respecting endianness.
     *
     * @param high the high byte
     * @param low the low byte
     * @param bigEndian true for big-endian byte order (default), false for little-endian
     * @return the combined Char value
     */
    fun toChar(high: Byte, low: Byte, bigEndian: Boolean = true): Char {
        return combineBytes(high, low, bigEndian).toChar()
    }

    /**
     * Combines two bytes into a Short, respecting endianness.
     *
     * @param high the high byte
     * @param low the low byte
     * @param bigEndian true for big-endian byte order (default), false for little-endian
     * @return the combined Short value
     */
    fun toShort(high: Byte, low: Byte, bigEndian: Boolean = true): Short = combineBytes(high, low, bigEndian).toShort()

    /**
     * Combines two bytes into an Int value, respecting endianness.
     *
     * @param high the high byte
     * @param low the low byte
     * @param bigEndian true for big-endian byte order (default), false for little-endian
     * @return the combined Int value
     */
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

    /**
     * Converts a byte array to Double, respecting endianness.
     *
     * @param bytes the byte array (must be exactly 8 bytes)
     * @param bigEndian true for big-endian byte order (default), false for little-endian
     * @return the Double value
     * @throws IllegalArgumentException if byte array size is not 8
     */
    fun toDouble(bytes: ByteArray, bigEndian: Boolean = true): Double {
        require(bytes.size == 8) { "Expected 8 bytes for double conversion" }
        val orderedBytes = if (bigEndian) bytes else bytes.reversedArray()
        var result = 0L
        for (b in orderedBytes) {
            result = (result shl 8) or (b.toLong() and 0xFF)
        }
        return Double.fromBits(result)
    }

    /**
     * Converts a byte array to Int, respecting endianness.
     *
     * @param bytes the byte array (must be exactly 4 bytes)
     * @param bigEndian true for big-endian byte order (default), false for little-endian
     * @return the Int value
     * @throws IllegalArgumentException if byte array size is not 4
     */
    fun toInt(bytes: ByteArray, bigEndian: Boolean = true): Int {
        require(bytes.size == 4) { "Expected 4 bytes for Int conversion" }
        val ordered = if (bigEndian) bytes else bytes.reversedArray()
        var result = 0
        for (b in ordered) {
            result = (result shl 8) or (b.toInt() and 0xFF)
        }
        return result
    }

    /**
     * Converts a byte array to Long, respecting endianness.
     *
     * @param bytes the byte array (must be exactly 8 bytes)
     * @param bigEndian true for big-endian byte order (default), false for little-endian
     * @return the Long value
     * @throws IllegalArgumentException if byte array size is not 8
     */
    fun toLong(bytes: ByteArray, bigEndian: Boolean = true): Long {
        require(bytes.size == 8) { "Expected 8 bytes for Long conversion" }
        val ordered = if (bigEndian) bytes else bytes.reversedArray()
        var result = 0L
        for (b in ordered) {
            result = (result shl 8) or (b.toLong() and 0xFF)
        }
        return result
    }

    /**
     * Converts a byte array to Float, respecting endianness.
     *
     * @param bytes the byte array (must be exactly 4 bytes)
     * @param bigEndian true for big-endian byte order (default), false for little-endian
     * @return the Float value
     * @throws IllegalArgumentException if byte array size is not 4
     */
    fun toFloat(bytes: ByteArray, bigEndian: Boolean = true): Float {
        require(bytes.size == 4) { "Expected 4 bytes for Float conversion" }
        val ordered = if (bigEndian) bytes else bytes.reversedArray()
        var intBits = 0
        for (b in ordered) {
            intBits = (intBits shl 8) or (b.toInt() and 0xFF)
        }
        return Float.fromBits(intBits)
    }

    /**
     * Converts a Char value to a byte array, respecting endianness.
     *
     * @param value the Char value to convert
     * @param bigEndian true for big-endian byte order, false for little-endian
     * @return the resulting 2-byte array
     */
    fun toByteArray(value: Char, bigEndian: Boolean): ByteArray {
        val v = value.code
        return if (bigEndian) {
            byteArrayOf((v shr 8).toByte(), v.toByte())
        } else {
            byteArrayOf(v.toByte(), (v shr 8).toByte())
        }
    }

    /**
     * Converts a Short value to a byte array, respecting endianness.
     *
     * @param value the Short value to convert
     * @param bigEndian true for big-endian byte order, false for little-endian
     * @return the resulting 2-byte array
     */
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

    /**
     * Converts an Int value to a byte array, respecting endianness.
     *
     * @param value the Int value to convert
     * @param bigEndian true for big-endian byte order, false for little-endian
     * @return the resulting 4-byte array
     */
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

    /**
     * Converts a Long value to a byte array, respecting endianness.
     *
     * @param value the Long value to convert
     * @param bigEndian true for big-endian byte order, false for little-endian
     * @return the resulting 8-byte array
     */
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