package id.feinn.utility.crypto.security

import kotlin.random.Random

/**
 * A mock implementation of SecureRandom for Kotlin Multiplatform.
 *
 * This class provides a method to fill a given [ByteArray] with pseudo-random signed bytes.
 *
 * ⚠️ WARNING: This is **not cryptographically secure**.
 * It uses [kotlin.random.Random], which is suitable only for non-security purposes such as simulations,
 * testing, or placeholder randomness.
 *
 */
public class SecureRandom {

    public fun nextBytes(bytes: ByteArray) {
        for (i in bytes.indices) bytes[i] = Random.nextInt(Byte.MIN_VALUE.toInt(), Byte.MAX_VALUE.toInt() + 1).toByte()
    }

}