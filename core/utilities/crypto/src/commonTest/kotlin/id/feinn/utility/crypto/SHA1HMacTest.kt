package id.feinn.utility.crypto

import kotlin.test.Test
import kotlin.test.assertTrue


class SHA1HMacTest {

    @Test
    fun `SHA1 HMAC Test`() {
        val key = "secret-key".encodeToByteArray()
        val message = "hello world".encodeToByteArray()
        val result = byteArrayOf(-72, 75, 0, 32, 119, 21, 38, 70, -90, -38, -110, 28, -11, -127, 33, 112, 81, 80, -87, 103)

        val digestFeinn = id.feinn.utility.crypto.digests.SHA1Digest()
        val hmacFeinn = id.feinn.utility.crypto.macs.HMac(digestFeinn)
        val keyParamFeinn = id.feinn.utility.crypto.params.KeyParameter(key)
        hmacFeinn.init(keyParamFeinn)
        hmacFeinn.update(message, 0, message.size)
        val resultFeinn = ByteArray(hmacFeinn.getMacSize())
        hmacFeinn.doFinal(resultFeinn, 0)
        println("SHA-1/HMAC Feinn: ${resultFeinn.joinToString()}")

        assertTrue(resultFeinn.contentEquals(result))
    }

}