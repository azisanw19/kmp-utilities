package id.feinn.utility.bytebuffer

/**
 * Thrown when a buffer put operation would exceed the buffer's limit.
 *
 * This runtime exception indicates an attempt to write more data to a buffer than it can hold,
 * either because:
 * - The current position is at or beyond the buffer's limit
 * - A bulk operation would write past the buffer's limit
 *
 * @see FeinnBuffer.nextPutIndex
 * @see FeinnBuffer.nextPutIndex
 *
 */
public class FeinnBufferOverflowException : RuntimeException()