package id.feinn.utility.bytebuffer

/**
 * Internal utility for comparing buffers and finding mismatches.
 */
internal object FeinnBufferMismatch {

    /**
     * Finds and returns the first position where two byte buffers differ within the specified range.
     *
     * Performs an optimized comparison by first checking the first byte (for longer comparisons)
     * before proceeding with a full element-by-element comparison.
     *
     * @param a the first buffer to compare
     * @param aOff the starting offset in the first buffer
     * @param b the second buffer to compare
     * @param bOff the starting offset in the second buffer
     * @param length the number of elements to compare
     * @return the index of the first mismatch, or -1 if the buffers are equal in the specified range
     *
     * @throws IndexOutOfBoundsException if either offset is negative or
     *         if length would cause access beyond buffer bounds
     */
    fun mismatch(a: FeinnByteBuffer, aOff: Int, b: FeinnByteBuffer, bOff: Int, length: Int): Int {
        if (length > 7 && a.get(aOff) != b.get(bOff)) return 0

        for (i in 0 until length) {
            if (a.get(aOff + i) != b.get(bOff + i)) {
                return i
            }
        }

        return -1
    }

}