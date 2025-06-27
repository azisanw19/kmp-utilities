package id.feinn.utility.crypto

import kotlin.test.Test
import kotlin.test.assertTrue

class AESCCMTest {

    @Test
    fun `AESCCM Encrypted Feinn`() {
        val keyParams = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        val nonce = byteArrayOf(17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28) // 11 bytes
        val data = byteArrayOf(37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52)
        val result = byteArrayOf(105, 107, 53, 30, -2, 44, 47, 30, 72, 63, -104, -73, 114, 94, -109, -22, -11, -73, -49, 54)

        val feinnAESEngine = id.feinn.utility.crypto.engine.AESEngine()
        val blockCipherFeinn = id.feinn.utility.crypto.modes.CCMBlockCipher(feinnAESEngine)
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
        println("Encrypted AESCCM: ${encryptedFeinn.joinToString()}")

        assertTrue(result.contentEquals(encryptedFeinn))
    }

    @Test
    fun `AESCCM decrypted Feinn`() {
        val keyParams = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        val nonce = byteArrayOf(17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28) // 11 bytes
        val data = byteArrayOf(37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52)
        val result = byteArrayOf(105, 107, 53, 30, -2, 44, 47, 30, 72, 63, -104, -73, 114, 94, -109, -22, -11, -73, -49, 54)

        val feinnAESEngine = id.feinn.utility.crypto.engine.AESEngine()
        val blockCipherFeinn = id.feinn.utility.crypto.modes.CCMBlockCipher(feinnAESEngine)
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