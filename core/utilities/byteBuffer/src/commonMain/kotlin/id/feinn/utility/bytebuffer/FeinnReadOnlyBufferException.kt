package id.feinn.utility.bytebuffer

/**
 * Thrown when a write operation is attempted on a read-only buffer.
 *
 * This unchecked exception indicates an attempt to modify the contents of a buffer
 * that has been marked as read-only. It extends [UnsupportedOperationException]
 * to maintain consistency with Java's buffer implementations.
 *
 * Common scenarios that trigger this exception:
 * - Calling any `put()` operation on a read-only buffer
 * - Attempting to modify the buffer's array() when read-only
 * - Any other mutating operation on a read-only buffer
 *
 * @see FeinnByteBuffer.isReadOnly
 * @see FeinnByteBuffer.array()
 */
public class FeinnReadOnlyBufferException : UnsupportedOperationException()
