package id.feinn.utility.time.exception

/**
 * A custom exception class that extends [Throwable] to represent errors related to date and time processing
 * in the [FeinnDate] system.
 *
 * This class provides several constructors for creating exceptions with different combinations of a message
 * and a cause, allowing for flexible error handling when working with date-time related operations.
 *
 * @constructor Creates a new instance of [FeinnDateTimeThrowable] with the specified message, cause, or both.
 *
 * Available constructors:
 * - [FeinnDateTimeThrowable(message: String)] - Creates an exception with the given message.
 * - [FeinnDateTimeThrowable(message: String, cause: Throwable)] - Creates an exception with the given message and cause.
 * - [FeinnDateTimeThrowable(cause: Throwable)] - Creates an exception with the given cause.
 * - [FeinnDateTimeThrowable()] - Creates an exception with no message or cause.
 *
 * Example usage:
 * ```
 * try {
 *     // Some operation that may throw an exception
 * } catch (e: FeinnDateTimeThrowable) {
 *     println("Error: ${e.message}")
 * }
 *
 * try {
 *     throw FeinnDateTimeThrowable("Invalid date format")
 * } catch (e: FeinnDateTimeThrowable) {
 *     println("Error: ${e.message}")
 * }
 * ```
 *
 * Note:
 * - This class is specifically intended to be used for date-time related exceptions in the [FeinnDate] system.
 */
public class FeinnDateTimeThrowable : Throwable {

    /**
     * Creates a new exception with the specified message.
     *
     * @param message String - The detail message to be reported for this exception.
     */
    public constructor(message: String) : super(message)

    /**
     * Creates a new exception with the specified message and cause.
     *
     * @param message String - The detail message to be reported for this exception.
     * @param cause Throwable - The cause of the exception (which can be retrieved later using [Throwable.getCause()]).
     */
    public constructor(message: String, cause: Throwable) : super(message, cause)

    /**
     * Creates a new exception with the specified cause.
     *
     * @param cause Throwable - The cause of the exception (which can be retrieved later using [Throwable.getCause()]).
     */
    public constructor(cause: Throwable) : super(cause)

    /**
     * Creates a new exception with no detail message or cause.
     */
    public constructor() : super()

    /**
     * Companion object for [FeinnDateTimeThrowable], which can hold any static properties or methods.
     */
    public companion object {}
}
