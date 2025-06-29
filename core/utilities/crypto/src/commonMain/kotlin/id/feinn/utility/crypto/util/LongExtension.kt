package id.feinn.utility.crypto.util

public fun Long.reverse(): Long {
    var x = this
    x = ((x ushr 1) and 0x5555555555555555) or ((x and 0x5555555555555555) shl 1)
    x = ((x ushr 2) and 0x3333333333333333) or ((x and 0x3333333333333333) shl 2)
    x = ((x ushr 4) and 0x0f0f0f0f0f0f0f0f) or ((x and 0x0f0f0f0f0f0f0f0f) shl 4)
    x = ((x ushr 8) and 0x00ff00ff00ff00ff) or ((x and 0x00ff00ff00ff00ff) shl 8)
    x = ((x ushr 16) and 0x0000ffff0000ffff) or ((x and 0x0000ffff0000ffff) shl 16)
    x = (x ushr 32) or (x shl 32)
    return x
}

public fun Long.reverseBytes(): Long {
    return ((this and 0xFFL) shl 56) or
            ((this ushr 8) and 0xFFL shl 48) or
            ((this ushr 16) and 0xFFL shl 40) or
            ((this ushr 24) and 0xFFL shl 32) or
            ((this ushr 32) and 0xFFL shl 24) or
            ((this ushr 40) and 0xFFL shl 16) or
            ((this ushr 48) and 0xFFL shl 8) or
            ((this ushr 56) and 0xFFL)
}
