package id.feinn.utility.crypto

import kotlin.test.Test
import kotlin.test.assertTrue


class SHA256HMacTest {

    @Test
    fun `SHA256 HMAC Test`() {
        val key = "secret-key".encodeToByteArray()
        val message = "hello world".encodeToByteArray()
        val result = byteArrayOf(9, 93, 90, 33, -2, 109, 6, 70, -37, 34, 63, -33, 61, -26, 67, 107, -72, -33, -78, -6, -80, -75, 22, 119, -20, -10, 68, 31, -49, 95, 42, 103)

        val digestFeinn = id.feinn.utility.crypto.digests.SHA256Digest()
        val hmacFeinn = id.feinn.utility.crypto.macs.HMac(digestFeinn)
        val keyParamFeinn = id.feinn.utility.crypto.params.KeyParameter(key)
        hmacFeinn.init(keyParamFeinn)
        hmacFeinn.update(message, 0, message.size)
        val resultFeinn = ByteArray(hmacFeinn.getMacSize())
        hmacFeinn.doFinal(resultFeinn, 0)
        println("SHA-256/HMAC Feinn: ${resultFeinn.joinToString()}")

        assertTrue(resultFeinn.contentEquals(result))
    }

}