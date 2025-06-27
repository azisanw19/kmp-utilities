package id.feinn.utility.crypto.params

import id.feinn.utility.crypto.CipherParameters

public class KeyParameter : CipherParameters {

    private lateinit var key: ByteArray

    public constructor(key: ByteArray): this(key, 0, key.size)

    public constructor(key: ByteArray, keyOff: Int, keyLen: Int): this(keyLen) {
        key.copyInto(this.key, destinationOffset = 0, startIndex = keyOff, endIndex = keyOff + keyLen)
    }

    private constructor(length: Int) {
        this.key = ByteArray(length)
    }

    public fun copyTo(buf: ByteArray, off: Int, len: Int) {
        if (key.size != len) throw IllegalArgumentException("len")

        key.copyInto(buf, destinationOffset = off, startIndex = 0, endIndex = 0 + len)
    }

    public fun getKey(): ByteArray = key

    public fun getKeyLength(): Int = key.size

    public fun reverse(): KeyParameter {
        val reversed = KeyParameter(key.size)
        reversed.key = this.key.reversedArray()
        return reversed
    }

}