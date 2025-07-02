package id.feinn.utility.crypto

import kotlin.test.Test
import kotlin.test.assertTrue

class AESCBCTest {

    @Test
    fun `AES CBC Encrypted`() {
        val keyParams = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        val nonce = byteArrayOf(17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32)
        val data = byteArrayOf(37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52)
        val result = byteArrayOf(70, 55, -128, 95, -100, 75, -124, -89, 39, -95, -16, -82, -39, -23, 99, -120, -116, 75, 127, -37, 113, -44, -119, -111, -88, -39, 58, -72, 102, 77, 67, 12)

        val feinnAESEngine = id.feinn.utility.crypto.engine.AESEngine()
        val blockCipherFeinn = id.feinn.utility.crypto.modes.CBCBlockCipher(feinnAESEngine)
        val paddingFeinn = id.feinn.utility.crypto.paddings.PKCS7Padding()
        val paddedBufferedFeinn = id.feinn.utility.crypto.paddings.PaddedBufferedBlockCipher(blockCipherFeinn, paddingFeinn)
        val paramsFeinn = id.feinn.utility.crypto.params.ParametersWithIV(
            id.feinn.utility.crypto.params.KeyParameter(keyParams),
            nonce
        )
        paddedBufferedFeinn.init(true, paramsFeinn)
        val encryptedFeinn = ByteArray(paddedBufferedFeinn.getOutputSize(data.size))
        val lenEncryptedFeinn = paddedBufferedFeinn.processBytes(
            data,
            0,
            data.size,
            encryptedFeinn,
            0
        )
        paddedBufferedFeinn.doFinal(encryptedFeinn, lenEncryptedFeinn)
        println("Encrypted Feinn: ${encryptedFeinn.joinToString()}")

        assertTrue(result.contentEquals(encryptedFeinn))
    }

    @Test
    fun `AES CBC dencrypted`() {
        val keyParams = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
        val nonce = byteArrayOf(17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32)
        val data = byteArrayOf(37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        val result = byteArrayOf(70, 55, -128, 95, -100, 75, -124, -89, 39, -95, -16, -82, -39, -23, 99, -120, -116, 75, 127, -37, 113, -44, -119, -111, -88, -39, 58, -72, 102, 77, 67, 12)

        val feinnAESEngine = id.feinn.utility.crypto.engine.AESEngine()
        val blockCipherFeinn = id.feinn.utility.crypto.modes.CBCBlockCipher(feinnAESEngine)
        val paddingFeinn = id.feinn.utility.crypto.paddings.PKCS7Padding()
        val paddedBufferedFeinn = id.feinn.utility.crypto.paddings.PaddedBufferedBlockCipher(blockCipherFeinn, paddingFeinn)
        val paramsFeinn = id.feinn.utility.crypto.params.ParametersWithIV(
            id.feinn.utility.crypto.params.KeyParameter(keyParams),
            nonce
        )
        paddedBufferedFeinn.init(false, paramsFeinn)
        val decryptedFeinn = ByteArray(paddedBufferedFeinn.getOutputSize(result.size))
        val lenDecryptedFeinn = paddedBufferedFeinn.processBytes(
            result,
            0,
            result.size,
            decryptedFeinn,
            0
        )
        paddedBufferedFeinn.doFinal(decryptedFeinn, lenDecryptedFeinn)
        println("Decrypted Feinn: ${decryptedFeinn.joinToString()}")

        assertTrue(data.contentEquals(decryptedFeinn))
    }

}