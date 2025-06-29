package id.feinn.utility.crypto.modes.gcm

import kotlin.properties.Delegates

public class BasicGCMExponentiator : GCMExponentiator {

    private var x by Delegates.notNull<LongArray>()

    override fun init(x: ByteArray) {
        this.x = GCMUtil.asLongs(x)
    }

    override fun exponentiateX(pow: Long, output: ByteArray) {
        var powT = pow
        val y = GCMUtil.oneAsLongs()

        if (powT > 0) {
            val powX = LongArray(GCMUtil.SIZE_LONGS)
            GCMUtil.copy(x, powX)

            do {
                if ((powT and 1L) != 0L) {
                    GCMUtil.multiply(y, powX)
                }
                GCMUtil.square(powX, powX)
                powT = powT ushr 1
            } while (powT > 0)

            GCMUtil.asBytes(y, output)
        }
    }

}