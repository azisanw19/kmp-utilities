package id.feinn.utility.crypto

import kotlin.test.Test
import kotlin.test.assertTrue

class AESGCMTest {

    @Test
    fun `AES GCM Encrypted`() {
        val keyParams = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        val nonce = byteArrayOf(17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28)
        val data = byteArrayOf(37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52)
        val result = byteArrayOf(18, 58, -15, 63, 17, -12, -24, 28, -42, 82, 68, 51, -120, 9, 49, 81, 80, 109, 82, 126)

        val feinnAESEngine = id.feinn.utility.crypto.engine.AESEngine()
        val blockCipherFeinn = id.feinn.utility.crypto.modes.GCMBlockCipher(feinnAESEngine)
        val paramsFeinn = id.feinn.utility.crypto.params.AEADParameters(
            id.feinn.utility.crypto.params.KeyParameter(keyParams),
            32,
            nonce
        )
        blockCipherFeinn.init(true, paramsFeinn)
        val encryptedFeinn = ByteArray(blockCipherFeinn.getOutputSize(data.size))
        val encryptedLenFeinn = blockCipherFeinn.processBytes(
            data,
            0,
            data.size,
            encryptedFeinn,
            0
        )
        blockCipherFeinn.doFinal(encryptedFeinn, encryptedLenFeinn)
        println("Encrypted Feinn: ${encryptedFeinn.joinToString()}")

        assertTrue(encryptedFeinn.contentEquals(result))
    }

    @Test
    fun `AES GCM decrypted`() {
        val keyParams = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        val nonce = byteArrayOf(17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28)
        val data = byteArrayOf(37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52)
        val result = byteArrayOf(18, 58, -15, 63, 17, -12, -24, 28, -42, 82, 68, 51, -120, 9, 49, 81, 80, 109, 82, 126)

        val feinnAESEngine = id.feinn.utility.crypto.engine.AESEngine()
        val blockCipherFeinn = id.feinn.utility.crypto.modes.GCMBlockCipher(feinnAESEngine)
        val paramsFeinn = id.feinn.utility.crypto.params.AEADParameters(
            id.feinn.utility.crypto.params.KeyParameter(keyParams),
            32,
            nonce
        )
        blockCipherFeinn.init(false, paramsFeinn)
        val decryptedFeinn = ByteArray(blockCipherFeinn.getOutputSize(result.size))
        val decryptedLenFeinn = blockCipherFeinn.processBytes(
            result,
            0,
            result.size,
            decryptedFeinn,
            0
        )
        blockCipherFeinn.doFinal(decryptedFeinn, decryptedLenFeinn)
        println("Decrypted AESCCM: ${decryptedFeinn.joinToString()}")

        assertTrue(decryptedFeinn.contentEquals(data))
    }

}