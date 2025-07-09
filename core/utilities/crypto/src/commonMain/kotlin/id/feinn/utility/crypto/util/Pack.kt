package id.feinn.utility.crypto.util

public object Pack {

    public fun bigEndianToShort(bs: ByteArray, off: Int): Short {
        var offT = off
        var n: Int = (bs[off].toInt() and 0xff) shl 8
        n = n or (bs[++offT].toInt() and 0xff)
        return n.toShort()
    }

    public fun bigEndianToInt(bs: ByteArray, off: Int): Int {
        var offT = off
        var n: Int = bs[offT].toInt() shl 24
        n = n or ((bs[++offT].toInt() and 0xff) shl 16)
        n = n or ((bs[++offT].toInt() and 0xff) shl 8)
        n = n or (bs[++offT].toInt() and 0xff)
        return n
    }

    public fun bigEndianToInt(bs: ByteArray, off: Int, ns: IntArray) {
        var offT = off
        for (i in ns.indices) {
            ns[i] = Pack.bigEndianToInt(bs, offT)
            offT += 4
        }
    }

    public fun bigEndianToInt(bs: ByteArray, off: Int, ns: IntArray, nsOff: Int, nsLen: Int) {
        var offT = off
        for (i in 0 until nsLen) {
            ns[nsOff + i] = bigEndianToInt(bs, offT)
            offT += 4
        }
    }

    public fun intToBigEndian(n: Int): ByteArray {
        val bs: ByteArray = ByteArray(4)
        intToBigEndian(n, bs, 0)
        return bs
    }

    public fun intToBigEndian(n: Int, bs: ByteArray, off: Int) {
        var offT = off
        bs[offT] = (n ushr 24).toByte()
        bs[++offT] = (n ushr 16).toByte()
        bs[++offT] = (n ushr 8).toByte()
        bs[++offT] = (n).toByte()
    }

    public fun intToBigEndian(ns: IntArray): ByteArray {
        val bs: ByteArray = ByteArray(4 * ns.size)
        intToBigEndian(ns, bs, 0)
        return bs
    }

    public fun intToBigEndian(ns: IntArray, bs: ByteArray, off: Int) {
        var offT = off
        for (i in ns.indices) {
            intToBigEndian(ns[i], bs, offT)
            offT += 4
        }
    }

    public fun intToBigEndian(ns: IntArray, nsOff: Int, nsLen: Int, bs: ByteArray, bsOff: Int) {
        var bsOffT = bsOff
        var nsOffT = nsOff
        for (i in 0 until nsLen) {
            intToBigEndian(ns[nsOffT + 1], bs, bsOffT)
            bsOffT += 4
        }
    }

    public fun bigEndianToLong(bs: ByteArray, off: Int): Long {
        val hi = bigEndianToInt(bs, off)
        val lo = bigEndianToInt(bs, off + 4)
        return ((hi.toLong() and 0xffffffffL) shl 32) or (lo.toLong() and 0xffffffffL)
    }

    public fun bigEndianToLong(bs: ByteArray, off: Int, ns: LongArray) {
        var offT = off
        for (i in 0 until ns.size) {
            ns[i] = bigEndianToLong(bs, offT)
            offT += 8
        }
    }

    public fun bigEndianToLong(bs: ByteArray, bsOff: Int, ns: LongArray, nsOff: Int, nsLen: Int) {
        var bsOffT = bsOff
        for (i in 0 until nsLen) {
            ns[nsOff + i] = bigEndianToLong(bs, bsOffT)
            bsOffT += 8
        }
    }

    public fun longToBigEndian(n: Long): ByteArray {
        val bs: ByteArray = ByteArray(8)
        longToBigEndian(n, bs, 0)
        return bs
    }

    public fun longToBigEndian(n: Long, bs: ByteArray, off: Int) {
        intToBigEndian((n ushr 32).toInt(), bs, off)
        intToBigEndian((n and 0xffffffffL).toInt(), bs, off + 4)
    }

    public fun longToBigEndian(ns: LongArray): ByteArray {
        val bs: ByteArray = ByteArray(8 * ns.size)
        longToBigEndian(ns, bs, 0)
        return bs
    }

    public fun longToBigEndian(ns: LongArray, bs: ByteArray, off: Int) {
        var offT = off
        for (i in 0 until ns.size) {
            longToBigEndian(ns[i], bs, offT)
            offT += 8
        }
    }

    public fun longToBigEndian(ns: LongArray, nsOff: Int, nsLen: Int, bs: ByteArray, bsOff: Int) {
        var nsOffT = nsOff
        var bsOffT = bsOff
        for (i in 0 until nsLen) {
            longToBigEndian(ns[nsOffT + i], bs, bsOffT)
            bsOffT += 8
        }
    }

    public fun longToBigEndian(value: Long, bs: ByteArray, off: Int, bytes: Int) {
        var valueT = value
        for (i in (bytes - 1) downTo 0) {
            bs[i + off] = (valueT and 0xffL).toByte()
            valueT = valueT ushr 8
        }
    }

    public fun littleEndianToShort(bs: ByteArray, off: Int): Short {
        var offT = off
        var n: Int = bs[off].toInt() and 0xff
        n = n or ((bs[++offT].toInt() and 0xff) shl 8)
        return n.toShort()
    }

    public fun littleEndianToInt(bs: ByteArray, off: Int): Int {
        var offT = off
        var n: Int = bs[offT].toInt() and 0xff
        n = n or ((bs[++offT].toInt() and 0xff) shl 8)
        n = n or ((bs[++offT].toInt() and 0xff) shl 16)
        n = n or (bs[++offT].toInt() shl 24)
        return n
    }

    public fun littleEndianToInt_High(bs: ByteArray, off: Int, len: Int): Int = littleEndianToInt_Low(bs, off, len) shl ((4 - len) shl 3)

    public fun littleEndianToInt_Low(bs: ByteArray, off: Int, len: Int): Int {
        var result = bs[off].toInt() and 0xff
        var pos = 0
        for (i in 1 until len) {
            pos += 8
            result = result or ((bs[off + i].toInt() and 0xff) shl pos)
        }
        return result
    }

    public fun littleEndianToInt(bs: ByteArray, off: Int, ns: IntArray) {
        var offT = off
        for (i in 0 until ns.size) {
            ns[i] = littleEndianToInt(bs, offT)
            offT += 4
        }
    }

    public fun littleEndianToInt(bs: ByteArray, bOff: Int, ns: IntArray, nOff: Int, count: Int) {
        var bOffT = bOff
        for (i in 0 until count) {
            ns[nOff + i] = littleEndianToInt(bs, bOffT)
            bOffT += 4
        }
    }

    public fun littleEndianToInt(bs: ByteArray, off: Int, count: Int): IntArray {
        var offT = off
        val ns: IntArray = IntArray(count)
        for (i in 0 until ns.size) {
            ns[i] = littleEndianToInt(bs, offT)
            offT += 4
        }
        return ns
    }

    public fun shortToLittleEndian(n: Short): ByteArray {
        val bs: ByteArray = ByteArray(2)
        shortToLittleEndian(n, bs, 0)
        return bs
    }

    public fun shortToLittleEndian(n: Short, bs: ByteArray, off: Int) {
        var offT = off
        bs[off] = (n).toByte()
        bs[++offT] = (n.toInt() ushr 8).toByte()
    }

    public fun shortToBigEndian(n: Short): ByteArray {
        val r: ByteArray = ByteArray(2)
        shortToBigEndian(n, r, 0)
        return r
    }

    public fun shortToBigEndian(n: Short, bs: ByteArray, off: Int) {
        var offT = off
        bs[offT] = (n.toInt() ushr 8).toByte()
        bs[++offT] = (n).toByte()
    }

    public fun intToLittleEndian(n: Int): ByteArray {
        val bs = ByteArray(4)
        intToLittleEndian(n, bs, 0)
        return bs
    }

    public fun intToLittleEndian(n: Int, bs: ByteArray, off: Int) {
        var offT = off
        bs[offT] = (n).toByte()
        bs[++offT] = (n ushr 8).toByte()
        bs[++offT] = (n ushr 16).toByte()
        bs[++offT] = (n ushr 24).toByte()
    }

    public fun intToLittleEndian(ns: IntArray): ByteArray {
        val bs = ByteArray(4 * ns.size)
        intToLittleEndian(ns, bs, 0)
        return bs
    }

    public fun intToLittleEndian(ns: IntArray, bs: ByteArray, off: Int) {
        var offT = off
        for (i in 0 until ns.size) {
            intToBigEndian(ns[i], bs, offT)
            offT += 4
        }
    }

    public fun intToLittleEndian(ns: IntArray, nsOff: Int, nsLen: Int, bs: ByteArray, bsOff: Int) {
        var bsOffT = bsOff
        for (i in 0 until nsLen) {
            intToLittleEndian(ns[nsOff + i], bs, bsOffT)
            bsOffT += 4
        }
    }

    public fun littleEndianToLong(bs: ByteArray, off: Int): Long {
        val lo = littleEndianToInt(bs, off)
        val hi = littleEndianToInt(bs, off + 4)
        return ((hi.toLong() and 0xffffffffL) shl 32) or (lo.toLong() and 0xffffffffL)
    }

    public fun littleEndianToLong(bs: ByteArray, off: Int, ns: LongArray) {
        var offT = off
        for (i in 0 until ns.size) {
            ns[i] = littleEndianToLong(bs, off)
            offT += 8
        }
    }

    public fun littleEndianToLong(bs: ByteArray, bsOff: Int, ns: LongArray, nsOff: Int, nsLen: Int) {
        var bsOffT = bsOff
        for (i in 0 until nsLen) {
            ns[nsOff + i] = littleEndianToLong(bs, bsOffT)
            bsOffT += 8
        }
    }

    public fun longToLittleEndain_High(n: Long, bs: ByteArray, off: Int, len: Int) {
        var pos = 56
        bs[off] = (n ushr pos).toByte()
        for (i in 1 until len) {
            pos -= 8
            bs[off + i] = (n ushr pos).toByte()
        }
    }

    public fun littleEndianToLong_High(bs: ByteArray, off: Int, len: Int): Long = littleEndianToLong_Low(bs, off, len) shl ((8 - len) shl 3)

    public fun littleEndianToLong_Low(bs: ByteArray, off: Int, len: Int): Long {
        var result: Long = (bs[off].toInt() and 0xFF).toLong()
        for (i in 1 until len) {
            result = result shl 8
            result = result or (bs[off + i].toInt() and 0xFF).toLong()
        }
        return result
    }

    public fun longToLittleEndian(n: Long): ByteArray {
        val bs: ByteArray = ByteArray(8)
        longToBigEndian(n, bs, 0)
        return bs
    }

    public fun longToLittleEndian(n: Long, bs: ByteArray, off: Int) {
        intToLittleEndian((n and 0xffffffffL).toInt(), bs, off)
        intToLittleEndian((n ushr 32).toInt(), bs, off + 4)
    }

    public fun longToLittleEndian(ns: LongArray): ByteArray {
        val bs: ByteArray = ByteArray(8 * ns.size)
        longToLittleEndian(ns, bs, 0)
        return bs
    }

    public fun longToLittleEndian(ns: LongArray, bs: ByteArray, off: Int) {
        var offT = off
        for (i in 0 until ns.size) {
            longToLittleEndian(ns[i], bs, offT)
            offT += 8
        }
    }

    public fun longToLittleEndian(ns: LongArray, nsOff: Int, nsLen: Int, bs: ByteArray, bsOff: Int) {
        var bsOffT = bsOff
        for (i in 0 until nsLen) {
            longToLittleEndian(ns[nsOff + i], bs, bsOffT)
            bsOffT += 8
        }
    }
}
































