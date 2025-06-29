package id.feinn.utility.crypto.modes.gcm

import id.feinn.utility.crypto.raw.Interleave
import id.feinn.utility.crypto.util.Longs
import id.feinn.utility.crypto.util.Pack

public object GCMUtil {

    public const val SIZE_BYTES: Int = 16
    public const val SIZE_INTS: Int = 4
    public const val SIZE_LONGS: Int = 2

    public const val E1: Int = 0xe1000000.toInt()
    public const val E1L: Long = (E1.toLong() and 0xFFFFFFFFL) shl 32

    public fun oneAsByte(): ByteArray {
        val tmp = ByteArray(SIZE_BYTES)
        tmp[0] = 0x80.toByte()
        return tmp
    }

    public fun oneAsInts(): IntArray {
        val tmp = IntArray(SIZE_INTS)
        tmp[0] = 1 shl 31
        return tmp
    }

    public fun oneAsLongs(): LongArray {
        val tmp = LongArray(SIZE_LONGS)
        tmp[0] = 1L shl 63
        return tmp
    }

    public fun areEqual(x: ByteArray, y: ByteArray): Byte {
        var d = 0
        for(i in 0 until SIZE_BYTES) d = d or (x[i].toInt() xor y[i].toInt())
        d = (d ushr 1) or (d and 1)
        return ((d - 1) shr 31).toByte()
    }

    public fun areEqual(x: IntArray, y: IntArray): Int {
        var d = 0
        d = d or (x[0] xor y[0])
        d = d or (x[1] xor y[1])
        d = d or (x[2] xor y[2])
        d = d or (x[3] xor y[3])
        d = (d ushr 1) or (d and 1)
        return (d - 1) shr 31
    }

    public fun areEqual(x: LongArray, y: LongArray): Long {
        var d = 0L
        d = d or (x[0] xor y[0])
        d = d or (x[1] xor y[1])
        d = (d ushr 1) or (d and 1L)
        return (d - 1L) shr 63
    }

    public fun asBytes(x: IntArray): ByteArray {
        val z = ByteArray(SIZE_BYTES)
        Pack.intToBigEndian(x, 0, SIZE_INTS, z, 0)
        return z
    }

    public fun asBytes(x: IntArray, z: ByteArray) {
        Pack.intToBigEndian(x, 0, SIZE_INTS, z, 0)
    }

    public fun asBytes(x: LongArray): ByteArray {
        val z = ByteArray(SIZE_BYTES)
        Pack.longToBigEndian(x, 0, SIZE_LONGS, z, 0)
        return z
    }

    public fun asBytes(x: LongArray, z: ByteArray) {
        Pack.longToBigEndian(x, 0, SIZE_LONGS, z, 0)
    }

    public fun asInts(x: ByteArray): IntArray {
        val z = IntArray(SIZE_INTS)
        Pack.bigEndianToInt(x, 0, z, 0, SIZE_INTS)
        return z
    }


    public fun asInts(x: ByteArray, z: IntArray) {
        Pack.bigEndianToInt(x, 0, z, 0, SIZE_INTS)
    }

    public fun asLongs(x: ByteArray): LongArray {
        val z = LongArray(SIZE_LONGS)
        Pack.bigEndianToLong(x, 0, z, 0, SIZE_LONGS)
        return z
    }

    public fun asLongs(x: ByteArray, z: LongArray) {
        Pack.bigEndianToLong(x, 0, z, 0, SIZE_LONGS)
    }

    public fun copy(x: ByteArray, z: ByteArray) {
        for (i in 0 until SIZE_BYTES) {
            z[i] = x[i]
        }
    }

    public fun copy(x: IntArray, z: IntArray) {
        z[0] = x[0]
        z[1] = x[1]
        z[2] = x[2]
        z[3] = x[3]
    }

    public fun copy(x: LongArray, z: LongArray) {
        z[0] = x[0]
        z[1] = x[1]
    }

    public fun divideP(x: LongArray, z: LongArray) {
        var x0 = x[0]; val x1 = x[1]
        val m = x0 shr 63
        x0 = x0 xor (m and E1L)
        z[0] = (x0 shl 1) or (x1 ushr 63)
        z[1] = (x1 shl 1) or -m
    }

    public fun multiply(x: ByteArray, y: ByteArray) {
        val t1 = asLongs(x)
        val t2 = asLongs(y)
        multiply(t1, t2)
        asBytes(t1, x)
    }

    public fun multiply(x: ByteArray, y: LongArray) {
        val x0: Long = Pack.bigEndianToLong(x, 0)
        val x1: Long = Pack.bigEndianToLong(x, 8)
        val y0: Long = y[0]; val y1 = y[1]
        val x0r: Long = Longs.reverse(x0); val x1r = Longs.reverse(x1)
        val y0r: Long = Longs.reverse(y0); val y1r = Longs.reverse(y1)

        val h0: Long = Longs.reverse(implMul64(x0r, y0r))
        val h1: Long = implMul64(x0, y0) shl 1
        val h2: Long = Longs.reverse(implMul64(x1r, y1r))
        val h3: Long = implMul64(x1, y1) shl 1
        val h4: Long = Longs.reverse(implMul64(x0r xor x1r, y0r xor y1r))
        val h5: Long = implMul64(x0 xor x1, y0 xor y1) shl 1

        var z0: Long = h0
        var z1: Long = h1 xor h0 xor h2 xor h4
        var z2: Long = h2 xor h1 xor h3 xor h5
        var z3: Long = h3

        z1 = z1 xor (z3 xor (z3 ushr 1) xor (z3 ushr 2) xor (z3 ushr 7))
        z2 = z2 xor ((z3 shl 62) xor (z3 shl 57))

        z0 = z0 xor (z2 xor (z2 ushr 1) xor (z2 ushr 2) xor (z2 ushr 7))
        z1 = z1 xor ((z2 shl 63) xor (z2 shl 62) xor (z2 shl 57))

        Pack.longToBigEndian(z0, x, 0)
        Pack.longToBigEndian(z1, x, 8)
    }

    public fun multiply(x: IntArray, y: IntArray) {
        var y0 = y[0]; var y1 = y[1]; var y2 = y[2]; var y3 = y[3]
        var z0 = 0; var z1 = 0; var z2 = 0; var z3 = 0

        for (i in 0 until SIZE_INTS) {
            var bits = x[i]
            for (j in 0 until 32) {
                val m1 = bits shr 31; bits = bits shl 1
                z0 = z0 xor (y0 and m1)
                z1 = z1 xor (y1 and m1)
                z2 = z2 xor (y2 and m1)
                z3 = z3 xor (y3 and m1)

                val m2 = (y3 shl 31) shr 8
                y3 = (y3 ushr 1) or (y2 shl 31)
                y2 = (y2 ushr 1) or (y1 shl 31)
                y1 = (y1 ushr 1) or (y0 shl 31)
                y0 = (y0 ushr 1) xor (m2 and E1)
            }
        }

        x[0] = z0
        x[1] = z1
        x[2] = z2
        x[3] = z3
    }

    public fun multiply(x: LongArray, y: LongArray) {
        val x0: Long = x[0]; val x1: Long = x[1]
        val y0: Long = y[0]; val y1: Long = y[1]
        val x0r: Long = Longs.reverse(x0); val x1r: Long = Longs.reverse(x1)
        val y0r: Long = Longs.reverse(y0); val y1r: Long = Longs.reverse(y1)

        val h0 = Longs.reverse(implMul64(x0r, y0r))
        val h1 = implMul64(x0, y0) shl 1
        val h2 = Longs.reverse(implMul64(x1r, y1r))
        val h3 = implMul64(x1, y1) shl 1
        val h4 = Longs.reverse(implMul64(x0r xor x1r, y0r xor y1r))
        val h5 = implMul64(x0 xor x1, y0 xor y1) shl 1

        var z0: Long = h0
        var z1: Long = h1 xor h0 xor h2 xor h4
        var z2: Long = h2 xor h1 xor h3 xor h5
        var z3: Long = h3

        z1 = z1 xor (z3 xor (z3 ushr 1) xor (z3 ushr 2) xor (z3 ushr 7))
        z2 = z2 xor ((z3 shl 62) xor (z3 shl 57))
        z0 = z0 xor (z2 xor (z2 ushr 1) xor (z2 ushr 2) xor (z2 ushr 7))
        z1 = z1 xor ((z2 shl 63) xor (z2 shl 62) xor (z2 shl 57))

        x[0] = z0
        x[1] = z1
    }

    public fun multiplyP(x: IntArray) {
        val x0 = x[0]; val x1 = x[1]; val x2 = x[2]; val x3 = x[3]
        val m = (x3 shl 31) shr 31
        x[0] = (x0 ushr 1) xor (m and E1)
        x[1] = (x1 ushr 1) or (x0 shl 31)
        x[2] = (x2 ushr 1) or (x1 shl 31)
        x[3] = (x3 ushr 1) or (x2 shl 31)
    }

    public fun multiplyP(x: IntArray, z: IntArray) {
        val x0 = x[0]; val x1 = x[1]; val x2 = x[2]; val x3 = x[3]
        val m = (x3 shl 31) shr 31
        z[0] = (x0 ushr 1) xor (m and E1)
        z[1] = (x1 ushr 1) or (x0 shl 31)
        z[2] = (x2 ushr 1) or (x1 shl 31)
        z[3] = (x3 ushr 1) or (x2 shl 31)
    }

    public fun multiplyP(x: LongArray) {
        val x0 = x[0]; val x1 = x[1]
        val m = (x1 shl 63) shr 63
        x[0] = (x0 ushr 1) xor (m and E1L)
        x[1] = (x1 ushr 1) or (x0 shl 63)
    }

    public fun multiplyP(x: LongArray, z: LongArray) {
        val x0 = x[0]; val x1 = x[1]
        val m = (x1 shl 63) shr 63
        z[0] = (x0 ushr 1) xor (m and E1L)
        z[1] = (x1 ushr 1) or (x0 shl 63)
    }

    public fun multiplyP3(x: LongArray, z: LongArray) {
        val x0 = x[0]; val x1 = x[1]
        val c = x1 shl 61
        z[0] = (x0 ushr 3) xor c xor (c ushr 1) xor (c ushr 2) xor (c ushr 7)
        z[1] = (x1 ushr 3) or (x0 shl 61)
    }

    public fun multiplyP4(x: LongArray, z: LongArray) {
        val x0 = x[0]; val x1 = x[1]
        val c = x1 shl 60
        z[0] = (x0 ushr 4) xor c xor (c ushr 1) xor (c ushr 2) xor (c ushr 7)
        z[1] = (x1 ushr 4) or (x0 shl 60)
    }

    public fun multiplyP7(x: LongArray, z: LongArray) {
        val x0 = x[0]; val x1 = x[1]
        val c = x1 shl 57
        z[0] = (x0 ushr 7) xor c xor (c ushr 1) xor (c ushr 2) xor (c ushr 7)
        z[1] = (x1 ushr 7) or (x0 shl 57)
    }

    public fun multiplyP8(x: IntArray) {
        val x0 = x[0]; val x1 = x[1]; val x2 = x[2]; val x3 = x[3]
        val c = x3 shl 24
        x[0] = (x0 ushr 8) xor c xor (c ushr 1) xor (c ushr 2) xor (c ushr 7)
        x[1] = (x1 ushr 8) or (x0 shl 24)
        x[2] = (x2 ushr 8) or (x1 shl 24)
        x[3] = (x3 ushr 8) or (x2 shl 24)
    }

    public fun multiplyP8(x: IntArray, y: IntArray) {
        val x0 = x[0]; val x1 = x[1]; val x2 = x[2]; val x3 = x[3]
        val c = x3 shl 24
        y[0] = (x0 ushr 8) xor c xor (c ushr 1) xor (c ushr 2) xor (c ushr 7)
        y[1] = (x1 ushr 8) or (x0 shl 24)
        y[2] = (x2 ushr 8) or (x1 shl 24)
        y[3] = (x3 ushr 8) or (x2 shl 24)
    }

    public fun multiplyP8(x: LongArray) {
        val x0 = x[0]; val x1 = x[1]
        val c = x1 shl 56
        x[0] = (x0 ushr 8) xor c xor (c ushr 1) xor (c ushr 2) xor (c ushr 7)
        x[1] = (x1 ushr 8) or (x0 shl 56)
    }

    public fun multiplyP8(x: LongArray, y: LongArray) {
        val x0 = x[0]; val x1 = x[1]
        val c = x1 shl 56
        y[0] = (x0 ushr 8) xor c xor (c ushr 1) xor (c ushr 2) xor (c ushr 7)
        y[1] = (x1 ushr 8) or (x0 shl 56)
    }

    public fun multiplyP16(x: LongArray) {
        val x0 = x[0]; val x1 = x[1]
        val c = x1 shl 48
        x[0] = (x0 ushr 16) xor c xor (c ushr 1) xor (c ushr 2) xor (c ushr 7)
        x[1] = (x1 ushr 16) or (x0 shl 48)
    }

    public fun pAsLongs(): LongArray {
        val tmp = LongArray(SIZE_LONGS)
        tmp[0] = 1L shl 62
        return tmp
    }

    public fun square(x: LongArray, z: LongArray) {
        val t = LongArray(SIZE_LONGS * 2)
        Interleave.expand64To128Rev(x[0], t, 0)
        Interleave.expand64To128Rev(x[1], t, 2)

        var z0 = t[0]
        var z1 = t[1]
        var z2 = t[2]
        val z3 = t[3]

        z1 = z1 xor (z3 xor (z3 ushr 1) xor (z3 ushr 2) xor (z3 ushr 7))
        z2 = z2 xor ((z3 shl 63) xor (z3 shl 62) xor (z3 shl 57))

        z0 = z0 xor (z2 xor (z2 ushr 1) xor (z2 ushr 2) xor (z2 ushr 7))
        z1 = z1 xor ((z2 shl 63) xor (z2 shl 62) xor (z2 shl 57))

        z[0] = z0
        z[1] = z1
    }

    public fun xor(x: ByteArray, y: ByteArray) {
        var i = 0
        do {
            x[i] = (x[i].toInt() xor y[i].toInt()).toByte(); ++i
            x[i] = (x[i].toInt() xor y[i].toInt()).toByte(); ++i
            x[i] = (x[i].toInt() xor y[i].toInt()).toByte(); ++i
            x[i] = (x[i].toInt() xor y[i].toInt()).toByte(); ++i
        } while (i < SIZE_BYTES)
    }

    public fun xor(x: ByteArray, y: ByteArray, yOff: Int) {
        var i = 0
        do {
            x[i] = (x[i].toInt() xor y[yOff + i].toInt()).toByte(); ++i
            x[i] = (x[i].toInt() xor y[yOff + i].toInt()).toByte(); ++i
            x[i] = (x[i].toInt() xor y[yOff + i].toInt()).toByte(); ++i
            x[i] = (x[i].toInt() xor y[yOff + i].toInt()).toByte(); ++i
        } while (i < SIZE_BYTES)
    }

    public fun xor(x: ByteArray, xOff: Int, y: ByteArray, yOff: Int, z: ByteArray, zOff: Int) {
        var i = 0
        do {
            z[zOff + i] = (x[xOff + i].toInt() xor y[yOff + i].toInt()).toByte(); ++i
            z[zOff + i] = (x[xOff + i].toInt() xor y[yOff + i].toInt()).toByte(); ++i
            z[zOff + i] = (x[xOff + i].toInt() xor y[yOff + i].toInt()).toByte(); ++i
            z[zOff + i] = (x[xOff + i].toInt() xor y[yOff + i].toInt()).toByte(); ++i
        } while (i < SIZE_BYTES)
    }

    public fun xor(x: ByteArray, y: ByteArray, yOff: Int, yLen: Int) {
        var yLenT = yLen
        while (--yLenT >= 0) x[yLen] = (x[yLenT].toInt() xor y[yOff + yLenT].toInt()).toByte()
    }

    public fun xor(x: ByteArray, xOff: Int, y: ByteArray, yOff: Int, len: Int) {
        var lenT = len
        while (--lenT >= 0) x[xOff + lenT] = (x[xOff + lenT].toInt() xor y[yOff + lenT].toInt()).toByte()
    }

    public fun xor(x: ByteArray, y: ByteArray, z: ByteArray) {
        var i = 0
        do {
            z[i] = (x[i].toInt() xor y[i].toInt()).toByte(); ++i
            z[i] = (x[i].toInt() xor y[i].toInt()).toByte(); ++i
            z[i] = (x[i].toInt() xor y[i].toInt()).toByte(); ++i
            z[i] = (x[i].toInt() xor y[i].toInt()).toByte(); ++i
        } while (i < SIZE_BYTES)
    }

    public fun xor(x: IntArray, y: IntArray) {
        x[0] = x[0] xor y[0]
        x[1] = x[1] xor y[1]
        x[2] = x[2] xor y[2]
        x[3] = x[3] xor y[3]
    }

    public fun xor(x: IntArray, y: IntArray, z: IntArray) {
        z[0] = x[0] xor y[0]
        z[1] = x[1] xor y[1]
        z[2] = x[2] xor y[2]
        z[3] = x[3] xor y[3]
    }

    public fun xor(x: LongArray, y: LongArray) {
        x[0] = x[0] xor y[0]
        x[1] = x[1] xor y[1]
    }

    public fun xor(x: LongArray, y: LongArray, z: LongArray) {
        z[0] = x[0] xor y[0]
        z[1] = x[1] xor y[1]
    }

    public fun implMul64(x: Long, y: Long): Long {
        val x0 = x and 0x1111111111111111L.toLong()
        val x1 = x and 0x2222222222222222L.toLong()
        val x2 = x and 0x4444444444444444L.toLong()
        val x3 = x and 0x8888888888888888UL.toLong()

        val y0 = y and 0x1111111111111111L.toLong()
        val y1 = y and 0x2222222222222222L.toLong()
        val y2 = y and 0x4444444444444444L.toLong()
        val y3 = y and 0x8888888888888888UL.toLong()

        val z0 = (x0 * y0) xor (x1 * y3) xor (x2 * y2) xor (x3 * y1) and 0x1111111111111111L.toLong()
        val z1 = (x0 * y1) xor (x1 * y0) xor (x2 * y3) xor (x3 * y2) and 0x2222222222222222L.toLong()
        val z2 = (x0 * y2) xor (x1 * y1) xor (x2 * y0) xor (x3 * y3) and 0x4444444444444444L.toLong()
        val z3 = (x0 * y3) xor (x1 * y2) xor (x2 * y1) xor (x3 * y0) and 0x8888888888888888UL.toLong()

        return z0 or z1 or z2 or z3
    }


}




































