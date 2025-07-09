package id.feinn.utility.crypto.modes.gcm

import id.feinn.utility.crypto.util.Pack
import kotlin.properties.Delegates

public class Tables4kGCMMultiplier: GCMMultiplier {

    private var H by Delegates.notNull<ByteArray>()
    private var T: List<LongArray>? = null

    override fun init(H: ByteArray) {
        if (T == null) T = List(256) { LongArray(2) }
        else if (0.toByte() != GCMUtil.areEqual(this.H, H)) return

        this.H = ByteArray(GCMUtil.SIZE_BYTES)
        GCMUtil.copy(H, this.H)

        GCMUtil.asLongs(this.H, T!![1])
        GCMUtil.multiplyP7(T!![1], T!![1])

        for (n in 2 until 256 step 2) {
            GCMUtil.divideP(T!![n shr 1], T!![n])

            GCMUtil.xor(T!![n], T!![1], T!![n + 1])
        }
    }

    override fun multiplyH(x: ByteArray) {
        var t = T!![x[15].toInt() and 0xFF]
        var z0 = t[0]; var z1 = t[1]

        for (i in 14 downTo 0) {
            t = T!![x[i].toInt() and 0xFF]

            val c = z1 shl 56
            z1 = t[1] xor ((z1 ushr 8) or (z0 shl 56))
            z0 = t[0] xor (z0 ushr 8) xor c xor (c ushr 1) xor (c ushr 2) xor (c ushr 7)
        }

        Pack.longToBigEndian(z0, x, 0)
        Pack.longToBigEndian(z1, x, 8)
    }

}