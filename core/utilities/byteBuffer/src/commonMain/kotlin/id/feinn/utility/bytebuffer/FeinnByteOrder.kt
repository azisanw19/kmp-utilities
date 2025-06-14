package id.feinn.utility.bytebuffer

/**
 * Represents the byte order (endianness) used when reading/writing multibyte values.
 *
 * This enum defines the two possible byte ordering schemes:
 * - `BIG_ENDIAN`: Most significant byte first (network byte order)
 * - `LITTLE_ENDIAN`: Least significant byte first
 *
 * Used by [FeinnByteBuffer] to determine the byte ordering for multibyte operations.
 *
 * Example usage:
 * ```
 * buffer.order(FeinnByteOrder.BIG_ENDIAN) // Set to big-endian mode
 * ```
 *
 * @see FeinnByteBuffer.order()
 * @see FeinnByteBuffer.order(FeinnByteOrder)
 */
public enum class FeinnByteOrder {
    /**
     * Little-endian byte order (least significant byte first).
     * Typical for x86/x64 processors.
     */
    LITTLE_ENDIAN,

    /**
     * Big-endian byte order (most significant byte first).
     * Also known as "network byte order".
     */
    BIG_ENDIAN
}