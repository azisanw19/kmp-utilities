package id.feinn.utility.crypto.paddings

import id.feinn.utility.crypto.InvalidCipherTextException
import id.feinn.utility.crypto.security.SecureRandom

public interface BlockCipherPadding {

    @Throws(IllegalArgumentException::class)
    public fun init(random: SecureRandom)

    public fun getPaddingName(): String

    public fun addPadding(`in`: ByteArray, inOff: Int): Int

    @Throws(InvalidCipherTextException::class)
    public fun padCount(`in`: ByteArray): Int

}