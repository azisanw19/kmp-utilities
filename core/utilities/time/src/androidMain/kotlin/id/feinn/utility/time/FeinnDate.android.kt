package id.feinn.utility.time

import id.feinn.utility.time.extension.getFormattedDate
import id.feinn.utility.time.extension.toDate
import id.feinn.utility.time.extension.toLocalDate
import java.text.ParseException
import java.time.LocalDate
import java.text.SimpleDateFormat
import java.util.Date

/**
 * A wrapper class for [LocalDate] that provides additional functionality and customization.
 *
 * The `FeinnDate` class encapsulates a [LocalDate] instance and provides overrides for common methods
 * such as [toString], [hashCode], and [equals], delegating their behavior to the underlying [LocalDate].
 * This class is designed for scenarios requiring extended features beyond the default [LocalDate] functionality.
 *
 * @constructor Creates a new instance of [FeinnDate]. By default, the [localDate] property is initialized
 *              to the current date ([LocalDate.now()]). Alternatively, a specific [LocalDate] can be passed
 *              via the secondary constructor.
 *
 * @property localDate LocalDate - The encapsulated [LocalDate] instance. Default value is the current date.
 *
 * ### Example Usage:
 * ```kotlin
 * // Create a new FeinnDate with the current date
 * val feinnDate = FeinnDate()
 * println(feinnDate) // Output: "2024-11-23" (or the current date)
 *
 * // Update the localDate property
 * feinnDate.localDate = LocalDate.of(2025, 1, 1)
 * println(feinnDate) // Output: "2025-01-01"
 *
 * // Create another FeinnDate instance and compare
 * val anotherDate = FeinnDate(LocalDate.of(2025, 1, 1))
 * println(feinnDate == anotherDate) // Output: true
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
public actual class FeinnDate() {

    /**
     * Companion object to hold any static properties or methods for [FeinnDate].
     *
     * This object can be used to provide factory methods or constants for the [FeinnDate] class.
     */
    public actual companion object {}

    /**
     * Secondary constructor to initialize [FeinnDate] with a specific [LocalDate].
     *
     * @param localDate The [LocalDate] instance to encapsulate.
     */
    public constructor(localDate: LocalDate) : this() {
        this.localDate = localDate
    }

    /**
     * The encapsulated [LocalDate] instance, defaulting to the current date.
     *
     * This property can be modified after the instance is created to update the date.
     */
    public var localDate: LocalDate = LocalDate.now()

    /**
     * Returns the string representation of the encapsulated [LocalDate].
     *
     * @return String - The string representation of [localDate] in ISO-8601 format (e.g., "2024-11-23").
     */
    override fun toString(): String {
        return localDate.toString()
    }

    /**
     * Returns the hash code of the encapsulated [LocalDate].
     *
     * @return Int - The hash code of [localDate].
     */
    override fun hashCode(): Int {
        return localDate.hashCode()
    }

    /**
     * Checks equality between this [FeinnDate] and another object.
     *
     * Two [FeinnDate] instances are considered equal if their [localDate] properties are equal.
     *
     * @param other Any? - The object to compare with.
     * @return Boolean - `true` if the other object is a [FeinnDate] with an equal [localDate], otherwise `false`.
     */
    override fun equals(other: Any?): Boolean {
        return localDate == (other as? FeinnDate)?.localDate
    }

}

/**
 * Creates a new instance of [FeinnDate] initialized with the current date.
 *
 * @receiver FeinnDate.Companion - The companion object of [FeinnDate].
 * @return FeinnDate - A new [FeinnDate] instance with [FeinnDate.localDate] set to the current date ([LocalDate.now()]).
 *
 * Example usage:
 * ```
 * val today = FeinnDate.now()
 * println(today) // Output: "2024-11-23" (or the current date)
 * ```
 *
 * Note: This method provides a convenient way to create a `FeinnDate` object with the current date
 * without needing to manually set the [FeinnDate.localDate] property.
 */
public actual fun FeinnDate.Companion.now(): FeinnDate {
    return FeinnDate()
}

/**
 * Formats the [FeinnDate.localDate] of this [FeinnDate] into a string representation based on the specified format and locale.
 *
 * @param format String - The desired format for the date, following the [SimpleDateFormat] pattern.
 *                        Examples: "yyyy-MM-dd", "dd MMMM yyyy", "hh:mm a".
 * @param locale FeinnLocale - The locale to be used for formatting, which affects elements like month and day names.
 * @return String - The formatted date as a string.
 *
 * This method internally converts the [FeinnDate.localDate] to a legacy [Date] object and then formats it
 * using the provided format and locale.
 *
 * Example usage:
 * ```
 * val feinnDate = FeinnDate().apply {
 *     localDate = LocalDate.of(2024, 11, 23)
 * }
 * val formattedDate = feinnDate.getFormattedDate("dd MMMM yyyy", FeinnLocale(Locale.ENGLISH))
 * println(formattedDate) // Output: "23 November 2024"
 * ```
 *
 * Note: Ensure that the format string is valid and the locale is correctly initialized to avoid errors.
 */
public actual fun FeinnDate.getFormattedDate(
    format: String,
    locale: FeinnLocale
): String {
    return this.localDate.toDate().getFormattedDate(format, locale.locale)
}

/**
 * Parses a date string into a [FeinnDate] object based on the specified format and locale.
 *
 * @receiver FeinnDate.Companion - The companion object of [FeinnDate].
 * @param date String - The string representation of the date to be parsed.
 *                      Example: "23-11-2024", "November 23, 2024".
 * @param format String - The expected format of the date string, following the [SimpleDateFormat] pattern.
 *                        Examples: "dd-MM-yyyy", "MMMM dd, yyyy".
 * @param locale FeinnLocale - The locale to be used for parsing, which affects elements like month and day names.
 * @return FeinnDate - A [FeinnDate] object initialized with the parsed date.
 *
 * This method converts the input string into a [LocalDate] using the given format and locale, then creates
 * a new [FeinnDate] object with the parsed date.
 *
 * Example usage:
 * ```
 * val dateString = "23-11-2024"
 * val feinnDate = FeinnDate.parse(dateString, "dd-MM-yyyy", FeinnLocale(Locale.ENGLISH))
 * println(feinnDate) // Output: "2024-11-23"
 * ```
 *
 * @throws ParseException if the date string cannot be parsed into a valid date using the provided format.
 *
 * Note: Ensure the input date string matches the specified format exactly to avoid errors during parsing.
 */
public actual fun FeinnDate.Companion.parse(
    date: String,
    format: String,
    locale: FeinnLocale
): FeinnDate {
    val localDate = date.toDate(format, locale.locale).toLocalDate()
    return FeinnDate(localDate)
}

public actual val FeinnDate.milliseconds: Long
    get() = this.localDate.toDate().time
