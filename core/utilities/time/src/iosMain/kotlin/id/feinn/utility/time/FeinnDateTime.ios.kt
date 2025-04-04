package id.feinn.utility.time

import id.feinn.utility.time.exception.FeinnDateTimeThrowable
import id.feinn.utility.time.extension.toStartDay
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.now
import platform.Foundation.timeIntervalSince1970

/**
 * A wrapper class for [NSDate] that represents a date and time, encapsulating platform-specific functionality for iOS.
 *
 * The `FeinnDateTime` class encapsulates an [NSDate] instance and provides methods for interacting with dates and times.
 * It includes functionality for formatting, parsing, and getting the current date and time, using platform-specific [NSDate] functionality.
 *
 * @property nsDate [NSDate] - The encapsulated [NSDate] instance that represents the date and time.
 *
 * Example usage:
 * ```
 * val currentDateTime = FeinnDateTime.now()
 * println(currentDateTime) // Output: Current NSDate representation
 *
 * val formattedDateTime = currentDateTime.getFormattedDateTime(FeinnDateTimeFormatter.ISO_LOCAL_DATE_TIME, FeinnLocale.getDefault())
 * println(formattedDateTime) // Output: Formatted date-time string
 * ```
 */
@Deprecated(
    message = "FeinnDateTime library is deprecated and no longer maintained. " +
            "Migrate to kotlinx-datetime for continued support. " +
            "Starting from version 1.0.0-alpha10, using FeinnDateTime will result in an error.",
    level = DeprecationLevel.WARNING,
    replaceWith = ReplaceWith(
        expression = "kotlinx-datetime",
        imports = ["kotlinx-datetime"]
    )
)
public actual class FeinnDateTime() {

    /**
     * Companion object for [FeinnDateTime], which provides static methods related to the class.
     */
    public actual companion object {}

    /**
     * Constructor to initialize [FeinnDateTime] with a custom [NSDate].
     *
     * @param nsDate [NSDate] - The [NSDate] instance to initialize the [FeinnDateTime] with.
     */
    public constructor(nsDate: NSDate) : this() {
        this.nsDate = nsDate
    }

    /**
     * The underlying [NSDate] instance that holds the date and time.
     */
    public var nsDate: NSDate = NSDate.now()

    /**
     * Returns a string representation of the current [FeinnDateTime] instance.
     *
     * This method formats the underlying [NSDate] object using the specified ISO 8601 date-time format.
     *
     * @return String - The string representation of the [nsDate] instance in ISO_LOCAL_DATE_TIME format.
     */
    override fun toString(): String {
        val nsDateFormatter = NSDateFormatter()
        nsDateFormatter.dateFormat = FeinnDateTimeFormatter.ISO_LOCAL_DATE_TIME
        nsDateFormatter.locale = FeinnLocale.getDefault().locale
        return nsDateFormatter.stringFromDate(nsDate)
    }

    /**
     * Returns the hash code for the current [FeinnDateTime] instance.
     *
     * This method overrides the default `hashCode()` to ensure correct behavior when [FeinnDateTime] instances are used
     * in collections like hash-based containers.
     *
     * @return Int - The hash code of the [nsDate] instance.
     */
    override fun hashCode(): Int {
        return nsDate.hashCode()
    }

    /**
     * Compares this [FeinnDateTime] instance with another object for equality.
     *
     * This method checks if two [FeinnDateTime] instances are equal by comparing their underlying [NSDate] objects.
     *
     * @param other Any? - The object to compare with.
     * @return Boolean - `true` if the objects have the same [nsDate], otherwise `false`.
     */
    override fun equals(other: Any?): Boolean {
        return nsDate == (other as? FeinnDateTime)?.nsDate
    }
}

/**
 * Returns a new [FeinnDateTime] instance representing the current date and time.
 *
 * This method provides a platform-specific implementation of getting the current date and time, using [NSDate.now()].
 *
 * @return [FeinnDateTime] - A new instance representing the current date and time.
 */
public actual fun FeinnDateTime.Companion.now(): FeinnDateTime {
    return FeinnDateTime()
}

/**
 * Formats the current [FeinnDateTime] instance to a string according to the specified format and locale.
 *
 * @param format String - The date-time format string (e.g., "yyyy-MM-dd'T'HH:mm:ss").
 * @param locale [FeinnLocale] - The locale to use for formatting.
 * @return String - The formatted date-time string.
 */
public actual fun FeinnDateTime.getFormattedDateTime(
    format: String,
    locale: FeinnLocale
): String {
    val nsDateFormatter = NSDateFormatter()
    nsDateFormatter.dateFormat = format
    nsDateFormatter.locale = locale.locale
    return nsDateFormatter.stringFromDate(this.nsDate)
}

/**
 * Parses a date-time string into a [FeinnDateTime] instance using the specified format and locale.
 *
 * @param date String - The date-time string to parse.
 * @param format String - The format to use for parsing (e.g., "yyyy-MM-dd'T'HH:mm:ss").
 * @param locale [FeinnLocale] - The locale to use for parsing.
 * @return [FeinnDateTime] - The parsed [FeinnDateTime] instance.
 * @throws FeinnDateTimeThrowable - If the parsing fails, an exception will be thrown.
 */
@Throws(FeinnDateTimeThrowable::class)
public actual fun FeinnDateTime.Companion.parse(
    date: String,
    format: String,
    locale: FeinnLocale
): FeinnDateTime {
    val nsDateFormatter = NSDateFormatter()
    nsDateFormatter.dateFormat = format
    nsDateFormatter.locale = locale.locale
    val nsDate = nsDateFormatter.dateFromString(date) ?: throw FeinnDateTimeThrowable("Failed to parse date time")
    return FeinnDateTime(nsDate)
}

/**
 * Converts the current [FeinnDateTime] to a [FeinnDate] instance, using the start of the current day.
 *
 * @return [FeinnDate] - A new instance representing the start of the current day.
 */
public actual fun FeinnDateTime.toFeinnDate(): FeinnDate {
    return FeinnDate(nsDate.toStartDay())
}

/**
 * Gets the number of milliseconds since the Unix epoch (January 1, 1970) for the current [FeinnDateTime] instance.
 *
 * This property retrieves the time interval (in seconds) since January 1, 1970, from the underlying [NSDate] object,
 * and then converts it to milliseconds by multiplying the result by 1000.
 *
 * @return Long - The number of milliseconds since the Unix epoch.
 */
public actual val FeinnDateTime.millisSeconds: Long
    get() = nsDate.timeIntervalSince1970.toLong()
