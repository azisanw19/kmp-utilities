package id.feinn.utility.crypto

import kotlin.test.Test
import kotlin.test.assertTrue

class AESCTRTest {

    @Test
    fun `AES CTR Encrypted`() {
        val keyParams = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        val nonce = byteArrayOf(17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28)
        val data = byteArrayOf(37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52)
        val result = byteArrayOf(-52, -75, -88, -72, -83, -39, -37, -23, -80, -56, 64, -10, 113, 107, 67, 58)

        val feinnAESEngine = id.feinn.utility.crypto.engine.AESEngine()
        val blockCipherFeinn = id.feinn.utility.crypto.modes.SICBlockCipher(feinnAESEngine)
        val paramsFeinn = id.feinn.utility.crypto.params.ParametersWithIV(
            id.feinn.utility.crypto.params.KeyParameter(keyParams),
            nonce
        )
        blockCipherFeinn.init(true, paramsFeinn)
        val encryptedFeinn = ByteArray(data.size)
        blockCipherFeinn.processBytes(
            data,
            0,
            data.size,
            encryptedFeinn,
            0
        )
        println("Encrypted Feinn: ${encryptedFeinn.joinToString()}")

        assertTrue(result.contentEquals(encryptedFeinn))
    }

    @Test
    fun `AES CTR decrypted`() {
        val keyParams = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        val nonce = byteArrayOf(17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28)
        val data = byteArrayOf(37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52)
        val result = byteArrayOf(-52, -75, -88, -72, -83, -39, -37, -23, -80, -56, 64, -10, 113, 107, 67, 58)

        val feinnAESEngine = id.feinn.utility.crypto.engine.AESEngine()
        val blockCipherFeinn = id.feinn.utility.crypto.modes.SICBlockCipher(feinnAESEngine)
        val paramsFeinn = id.feinn.utility.crypto.params.ParametersWithIV(
            id.feinn.utility.crypto.params.KeyParameter(keyParams),
            nonce
        )
        blockCipherFeinn.init(false, paramsFeinn)
        val dencryptedFeinn = ByteArray(result.size)
        blockCipherFeinn.processBytes(
            result,
            0,
            result.size,
            dencryptedFeinn,
            0
        )
        println("Encrypted Feinn: ${dencryptedFeinn.joinToString()}")

        assertTrue(data.contentEquals(dencryptedFeinn))
    }

}