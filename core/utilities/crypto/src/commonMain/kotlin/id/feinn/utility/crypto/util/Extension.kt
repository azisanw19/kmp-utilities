package id.feinn.utility.crypto.util

public fun String.toByteArray(): ByteArray {
    require(length % 2 == 0) { "Hex string must have even length" }
    require(matches(Regex("^[0-9a-fA-F]+$"))) { "Hex string contains invalid characters" }

    return ByteArray(length / 2) { i ->
        val index = i * 2
        val byte = substring(index, index + 2).toInt(16)
        byte.toByte()
    }
}

public fun ByteArray.toHex(lowercase: Boolean = true): String {
    val sb = StringBuilder(size * 2)
    for (byte in this) {
        val hex = (byte.toInt() and 0xFF).toString(16).padStart(2, '0')
        sb.append(if (lowercase) hex else hex.uppercase())
    }
    return sb.toString()
}