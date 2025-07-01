package id.feinn.utility.crypto.paddings

import id.feinn.utility.crypto.InvalidCipherTextException
import id.feinn.utility.crypto.security.SecureRandom

public class PKCS7Padding : BlockCipherPadding {

    override fun init(random: SecureRandom?) {
        // nothing to do
    }

    override fun getPaddingName(): String = "PKCS7"

    override fun addPadding(`in`: ByteArray, inOff: Int): Int {
        var inOffT = inOff
        val code = (`in`.size - inOffT).toByte()

        while (inOffT < `in`.size) {
            `in`[inOffT] = code
            inOffT++
        }

        return code.toInt()
    }

    override fun padCount(`in`: ByteArray): Int {
        val countAsByte = `in`[`in`.size - 1]
        val count = countAsByte.toInt() and 0xFF
        val position = `in`.size - count

        var failed = (position or (count - 1)) shr 31
        for (i in 0 until `in`.size) failed = failed or ((`in`[i].toInt() xor countAsByte.toInt()) and ((i - position) shr 31).inv())
        if (failed != 0) throw InvalidCipherTextException("pod bloack corrupted")

        return count
    }

}