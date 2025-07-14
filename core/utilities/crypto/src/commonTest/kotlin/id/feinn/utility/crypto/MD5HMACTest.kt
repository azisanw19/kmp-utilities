package id.feinn.utility.crypto

import kotlin.test.Test
import kotlin.test.assertTrue

class MD5HMACTest {

    @Test
    fun `MD5 HMAC`() {
        val key = "secret-key".encodeToByteArray()
        val message = "hello world".encodeToByteArray()
        val result = byteArrayOf(54, -72, -102, -86, 114, 86, 6, 56, -123, 39, 3, -84, -70, 72, 75, 43)

        val digestFeinn = id.feinn.utility.crypto.digests.MD5Digest()
        val hmacFeinn = id.feinn.utility.crypto.macs.HMac(digestFeinn)
        val keyParamFeinn = id.feinn.utility.crypto.params.KeyParameter(key)
        hmacFeinn.init(keyParamFeinn)
        hmacFeinn.update(message, 0, message.size)
        val resultFeinn = ByteArray(hmacFeinn.getMacSize())
        hmacFeinn.doFinal(resultFeinn, 0)
        println("MD5/HMAC Feinn: ${resultFeinn.joinToString()}")

        assertTrue(resultFeinn.contentEquals(result))
    }

}