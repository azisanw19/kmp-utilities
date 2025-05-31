package id.feinn.utility.bytebuffer

internal object BufferMismatch {

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