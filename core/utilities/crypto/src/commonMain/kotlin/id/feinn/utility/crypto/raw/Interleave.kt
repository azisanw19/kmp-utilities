package id.feinn.utility.crypto.raw

public object Interleave {

    private const val M32: Long = 0x55555555L
    private const val M64: Long = 0x5555555555555555L
    private val M64R: Long = 0xAAAAAAAAAAAAAAAAUL.toLong()

    public fun expand8to16(x: Int): Int {
        var xT = x
        xT = xT and 0xFF
        xT = (xT or (xT shl 4)) and 0x0F0F
        xT = (xT or (xT shl 2)) and 0x3333
        xT = (xT or (xT shl 1)) and 0x5555
        return xT
    }

    public fun expand16to32(x: Int): Int {
        var xT = x
        xT = xT and 0xFFFF
        xT = (xT or (xT shl 8)) and 0x00FF00FF
        xT = (xT or (xT shl 4)) and 0x0F0F0F0F
        xT = (xT or (xT shl 2)) and 0x33333333
        xT = (xT or (xT shl 1)) and 0x55555555
        return xT
    }

    public fun expand32to64(x: Int): Long {
        var xT = x

        // "shuffle" low half to even bits and high half to odd bits
        xT = Bits.bitPermuteStep(xT, 0x0000FF00, 8)
        xT = Bits.bitPermuteStep(xT, 0x00F000F0, 4)
        xT = Bits.bitPermuteStep(xT, 0x0C0C0C0C, 2)
        xT = Bits.bitPermuteStep(xT, 0x22222222, 1)

        return ((xT ushr 1).toLong() and M32) shl 32 or (xT.toLong() and M32)
    }

    public fun expand64To128(x: Long, z: LongArray, zOff: Int) {
        var xT = x

        xT = Bits.bitPermuteStep(xT, 0x00000000FFFF0000L, 16)
        xT = Bits.bitPermuteStep(xT, 0x0000FF000000FF00L, 8)
        xT = Bits.bitPermuteStep(xT, 0x00F000F000F000F0L, 4)
        xT = Bits.bitPermuteStep(xT, 0x0C0C0C0C0C0C0C0CL, 2)
        xT = Bits.bitPermuteStep(xT, 0x2222222222222222L, 1)

        z[zOff] = (xT) and M64
        z[zOff + 1] = (xT ushr 1) and M64
    }

    public fun expand64To128(xs: LongArray, xsOff: Int, xsLen: Int, zs: LongArray, zsOff: Int) {
        var zsOffT = zsOff
        for (i in 0 until xsLen) {
            expand64To128(xs[xsOff + i], zs, zsOffT)
            zsOffT += 2
        }
    }

    public fun expand64To128Rev(x: Long, z: LongArray, zOff: Int) {
        var xT = x
        xT = Bits.bitPermuteStep(xT, 0x00000000FFFF0000L, 16)
        xT = Bits.bitPermuteStep(xT, 0x0000FF000000FF00L, 8)
        xT = Bits.bitPermuteStep(xT, 0x00F000F000F000F0L, 4)
        xT = Bits.bitPermuteStep(xT, 0x0C0C0C0C0C0C0C0CL, 2)
        xT = Bits.bitPermuteStep(xT, 0x2222222222222222L, 1)

        z[zOff] = (xT) and M64R
        z[zOff + 1] = (xT shl 1) and M64R
    }

    public fun shuffle(x: Int): Int {
        var xT = x
        xT = Bits.bitPermuteStep(xT, 0x0000FF00, 8)
        xT = Bits.bitPermuteStep(xT, 0x00F000F0, 4)
        xT = Bits.bitPermuteStep(xT, 0x0C0C0C0C, 2)
        xT = Bits.bitPermuteStep(xT, 0x22222222, 1)
        return xT
    }

    public fun shuffle2(x: Int): Int {
        var xT = x
        xT = Bits.bitPermuteStep(xT, 0x00AA00AA, 7)
        xT = Bits.bitPermuteStep(xT, 0x0000CCCC, 14)
        xT = Bits.bitPermuteStep(xT, 0x00F000F0, 4)
        xT = Bits.bitPermuteStep(xT, 0x0000FF00, 8)
        return xT
    }

    public fun shuffle2(x: Long): Long {
        var xT = x
        xT = Bits.bitPermuteStep(xT, 0x00000000FF00FF00L, 24)
        xT = Bits.bitPermuteStep(xT, 0x00CC00CC00CC00CCL, 6)
        xT = Bits.bitPermuteStep(xT, 0x0000F0F00000F0F0L, 12)
        xT = Bits.bitPermuteStep(xT, 0x0A0A0A0A0A0A0A0AL, 3)
        return xT
    }

    public fun shuffle3(x: Long): Long {
        var xT = x
        xT = Bits.bitPermuteStep(xT, 0x00AA00AA00AA00AAL, 7)
        xT = Bits.bitPermuteStep(xT, 0x0000CCCC0000CCCCL, 14)
        xT = Bits.bitPermuteStep(xT, 0x00000000F0F0F0F0L, 28)
        return xT
    }

    public fun unshuffle(x: Int): Int {
        var xT = x
        xT = Bits.bitPermuteStep(xT, 0x22222222, 1)
        xT = Bits.bitPermuteStep(xT, 0x0C0C0C0C, 2)
        xT = Bits.bitPermuteStep(xT, 0x00F000F0, 4)
        xT = Bits.bitPermuteStep(xT, 0x0000FF00, 8)
        return xT
    }

    public fun unshuffle(x: Long): Long {
        var xT = x
        xT = Bits.bitPermuteStep(xT, 0x2222222222222222L, 1)
        xT = Bits.bitPermuteStep(xT, 0x0C0C0C0C0C0C0C0CL, 2)
        xT = Bits.bitPermuteStep(xT, 0x00F000F000F000F0L, 4)
        xT = Bits.bitPermuteStep(xT, 0x0000FF000000FF00L, 8)
        xT = Bits.bitPermuteStep(xT, 0x00000000FFFF0000L, 16)
        return xT
    }

    public fun unshuffle2(x: Int): Int {
        var xT = x
        xT = Bits.bitPermuteStep(xT, 0x0000FF00, 8)
        xT = Bits.bitPermuteStep(xT, 0x00F000F0, 4)
        xT = Bits.bitPermuteStep(xT, 0x0000CCCC, 14)
        xT = Bits.bitPermuteStep(xT, 0x00AA00AA, 7)
        return xT
    }

    public fun unshuffle2(x: Long): Long {
        var xT = x
        xT = Bits.bitPermuteStep(xT, 0x0A0A0A0A0A0A0A0AL, 3)
        xT = Bits.bitPermuteStep(xT, 0x0000F0F00000F0F0L, 12)
        xT = Bits.bitPermuteStep(xT, 0x00CC00CC00CC00CCL, 6)
        xT = Bits.bitPermuteStep(xT, 0x00000000FF00FF00L, 24)
        return xT
    }



}