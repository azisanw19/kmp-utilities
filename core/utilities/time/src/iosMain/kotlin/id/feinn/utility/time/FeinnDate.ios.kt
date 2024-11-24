package id.feinn.utility.time

import id.feinn.utility.time.exception.FeinnDateTimeThrowable
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.now
import platform.Foundation.timeIntervalSince1970

/**
 * The actual implementation of [FeinnDate] for iOS, representing a date using [NSDate].
 *
 * This class encapsulates the platform-specific [NSDate] object from the iOS SDK. It provides functionality
 * to interact with dates, including formatting, parsing, and getting the current date.
 * The `nsDate` property stores the actual iOS date instance.
 *
 * ### Example usage:
 * ```kotlin
 * val currentDate = FeinnDate.now()
 * println(currentDate) // Output: Current NSDate representation
 * ```
 */
public actual class FeinnDate() {

    /**
     * The companion object for [FeinnDate], which provides static methods related to the class.
     */
    public actual companion object {}

    public constructor(nsDate: NSDate) : this() {
        this.nsDate = nsDate
    }

    /**
     * The actual [NSDate] object that holds the date value.
     *
     * This property encapsulates the native iOS [NSDate] instance that represents the date and time.
     * It is initialized to the current date and time by default.
     */
    public var nsDate: NSDate = NSDate.now()

    /**
     * Returns a string representation of the current [FeinnDate] instance.
     *
     * This method overrides the default `toString()` method to provide a meaningful string output for the [FeinnDate] object.
     * The string is formatted using the ISO_LOCAL_DATE format (yyyy-MM-dd) by default.
     *
     * @return String - The string representation of the [nsDate] instance in the format "yyyy-MM-dd".
     */
    override fun toString(): String {
        val nsDateFormatter = NSDateFormatter()
        nsDateFormatter.dateFormat = FeinnDateTimeFormatter.ISO_LOCAL_DATE
        nsDateFormatter.locale = FeinnLocale.getDefault().locale
        return nsDateFormatter.stringFromDate(nsDate)
    }

    /**
     * Returns the hash code for the current [FeinnDate] instance.
     *
     * This method overrides the default `hashCode()` method to return the hash code of the underlying
     * [NSDate] object, ensuring correct behavior when [FeinnDate] instances are used in collections like
     * hash-based containers (e.g., HashSet, HashMap).
     *
     * @return Int - The hash code of the [nsDate] instance.
     */
    override fun hashCode(): Int {
        return nsDate.hashCode()
    }

    /**
     * Compares this [FeinnDate] instance with another object for equality.
     *
     * This method overrides the default `equals()` method to check if two [FeinnDate] instances are equal
     * by comparing their underlying [NSDate] objects. It ensures that [FeinnDate] instances are considered
     * equal if they represent the same date and time.
     *
     * @param other Any? - The object to compare with.
     * @return Boolean - True if both objects are [FeinnDate] instances and have the same [nsDate], false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        return nsDate == (other as? FeinnDate)?.nsDate
    }
}

/**
 * Returns the current date and time as a [FeinnDate] instance.
 *
 * This method creates a new [FeinnDate] object and sets its `nsDate` property to the current date and time
 * using the [NSDate.now()] method. It provides the current date encapsulated as a [FeinnDate] object.
 *
 * @return FeinnDate - A new instance of [FeinnDate] representing the current date and time.
 *
 * Example usage:
 * ```
 * val currentDate = FeinnDate.now()
 * println(currentDate) // Output: Current date and time in the [NSDate] format
 * ```
 */
public actual fun FeinnDate.Companion.now(): FeinnDate {
    return FeinnDate()
}

/**
 * Formats the current [FeinnDate] instance into a string according to the specified format and locale.
 *
 * This method formats the [FeinnDate] instance's underlying [NSDate] using the provided date format and
 * locale. It utilizes the [NSDateFormatter] class to perform the formatting based on the given format and
 * locale.
 *
 * @param format String - The format string to be used for date formatting. Example: "yyyy-MM-dd", "dd/MM/yyyy".
 * @param locale FeinnLocale - The locale to be used for formatting. The default locale will be used if not provided.
 * @return String - The formatted date as a string.
 *
 * Example usage:
 * ```
 * val feinnDate = FeinnDate.now()
 * val formattedDate = feinnDate.getFormattedDate("dd-MM-yyyy", FeinnLocale.getDefault())
 * println(formattedDate) // Output: "23-11-2024"
 * ```
 */
public actual fun FeinnDate.getFormattedDate(
    format: String,
    locale: FeinnLocale
): String {
    val nsDateFormatter = NSDateFormatter()
    nsDateFormatter.dateFormat = format
    nsDateFormatter.locale = locale.locale
    return nsDateFormatter.stringFromDate(this.nsDate)
}

/**
 * Parses a date string into a [FeinnDate] instance based on the specified format and locale.
 *
 * This method converts a date string into a [FeinnDate] instance by parsing it using the provided format
 * and locale. If parsing fails (i.e., the string does not match the format), a [FeinnDateTimeThrowable] is thrown.
 *
 * @param date String - The date string to be parsed.
 * @param format String - The format string to be used for parsing. Example: "yyyy-MM-dd", "dd/MM/yyyy".
 * @param locale FeinnLocale - The locale to be used for parsing. The default locale will be used if not provided.
 * @return FeinnDate - A new [FeinnDate] instance representing the parsed date.
 * @throws FeinnDateTimeThrowable - If parsing fails, an exception is thrown.
 *
 * Example usage:
 * ```
 * val dateString = "23-11-2024"
 * val feinnDate = FeinnDate.parse(dateString, "dd-MM-yyyy", FeinnLocale.getDefault())
 * println(feinnDate) // Output: FeinnDate object representing "2024-11-23"
 * ```
 */
@Throws(FeinnDateTimeThrowable::class)
public actual fun FeinnDate.Companion.parse(
    date: String,
    format: String,
    locale: FeinnLocale
): FeinnDate {
    val dateFormatter = NSDateFormatter()
    dateFormatter.dateFormat = format
    dateFormatter.locale = locale.locale
    val nsDate = dateFormatter.dateFromString(date) ?: throw FeinnDateTimeThrowable("Failed to parse date")
    return FeinnDate(nsDate)
}

/**
 * Gets the number of milliseconds since the Unix epoch (January 1, 1970) for the current [FeinnDate] instance.
 *
 * This property retrieves the time interval (in seconds) since January 1, 1970, from the underlying [NSDate] object,
 * then converts it to milliseconds by multiplying the result by 1000.
 *
 * This can be useful in scenarios where a precise timestamp (in milliseconds) is required, such as:
 * - Working with time-based comparisons.
 * - Sending timestamps in APIs that expect milliseconds as the unit.
 * - Handling precise time calculations in applications.
 *
 * @return Long - The number of milliseconds since the Unix epoch (January 1, 1970).
 */
public actual val FeinnDate.milliseconds: Long
    get() = nsDate.timeIntervalSince1970.toLong() * 1000L
