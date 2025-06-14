package id.feinn.utility.bytebuffer

/**
 * Thrown when a buffer get operation would exceed the buffer's limit.
 *
 * This runtime exception indicates an attempt to read more data from a buffer than is available,
 * typically occurring when:
 * - The current position is at or beyond the buffer's limit
 * - A bulk read operation would read past the buffer's limit
 * - Attempting to read from an empty buffer
 *
 * @see FeinnBuffer.nextGetIndex
 * @see FeinnBuffer.nextGetIndex
 */
public class FeinnBufferUnderflowException : RuntimeException()