package id.feinn.utility.time

import id.feinn.utility.time.extension.getFormattedDate
import id.feinn.utility.time.extension.toDate
import id.feinn.utility.time.extension.toLocalDateTime
import java.time.LocalDateTime
import id.feinn.utility.time.exception.FeinnDateTimeThrowable

/**
 * A wrapper class for [LocalDateTime] that provides additional functionality and customization.
 *
 * The `FeinnDateTime` class encapsulates a [LocalDateTime] instance and provides utility methods for
 * date-time formatting, parsing, and conversions to other date-time types.
 * It also overrides common methods such as [toString], [hashCode], and [equals].
 *
 * @constructor Creates a new instance of [FeinnDateTime]. By default, the [localDateTime] property is initialized
 *              to the current date-time ([LocalDateTime.now()]). Alternatively, a specific [LocalDateTime] can
 *              be passed via the secondary constructor.
 *
 * @property localDateTime LocalDateTime - The encapsulated [LocalDateTime] instance. Default value is the current date-time.
 *
 * ### Example Usage:
 * ```kotlin
 * // Create a new FeinnDateTime with the current date-time
 * val feinnDateTime = FeinnDateTime()
 * println(feinnDateTime) // Output: "2024-11-24T12:34:56" (or the current date-time)
 *
 * // Update the localDateTime property
 * feinnDateTime.localDateTime = LocalDateTime.of(2025, 1, 1, 10, 0)
 * println(feinnDateTime) // Output: "2025-01-01T10:00:00"
 *
 * // Convert to FeinnDate
 * val feinnDate = feinnDateTime.toFeinnDate()
 * println(feinnDate) // Output: "2025-01-01"
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
     * Companion object to hold static properties and methods for [FeinnDateTime].
     */
    public actual companion object {}

    /**
     * Secondary constructor to initialize [FeinnDateTime] with a specific [LocalDateTime].
     *
     * @param localDateTime The [LocalDateTime] instance to encapsulate.
     */
    public constructor(localDateTime: LocalDateTime) : this() {
        this.localDateTime = localDateTime
    }

    /**
     * The encapsulated [LocalDateTime] instance, defaulting to the current date-time.
     */
    public var localDateTime: LocalDateTime = LocalDateTime.now()

    /**
     * Returns the string representation of the encapsulated [LocalDateTime].
     *
     * @return String - The string representation of [localDateTime] in ISO-8601 format (e.g., "2024-11-24T12:34:56").
     */
    override fun toString(): String {
        return localDateTime.toString()
    }

    /**
     * Returns the hash code of the encapsulated [LocalDateTime].
     *
     * @return Int - The hash code of [localDateTime].
     */
    override fun hashCode(): Int {
        return localDateTime.hashCode()
    }

    /**
     * Checks equality between this [FeinnDateTime] and another object.
     *
     * Two [FeinnDateTime] instances are considered equal if their [localDateTime] properties are equal.
     *
     * @param other Any? - The object to compare with.
     * @return Boolean - `true` if the other object is a [FeinnDateTime] with an equal [localDateTime], otherwise `false`.
     */
    override fun equals(other: Any?): Boolean {
        return localDateTime == (other as? FeinnDateTime)?.localDateTime
    }
}

/**
 * Returns the current date-time as a [FeinnDateTime].
 *
 * @return FeinnDateTime - A [FeinnDateTime] instance initialized to the current date-time.
 */
public actual fun FeinnDateTime.Companion.now(): FeinnDateTime {
    return FeinnDateTime()
}

/**
 * Formats the [FeinnDateTime] instance into a string based on the specified format and locale.
 *
 * @param format String - The date-time format (e.g., "yyyy-MM-dd HH:mm:ss").
 * @param locale FeinnLocale - The locale to use for formatting.
 * @return String - The formatted date-time string.
 *
 * @throws FeinnDateTimeThrowable if formatting fails.
 *
 * ### Example:
 * ```kotlin
 * val feinnDateTime = FeinnDateTime()
 * val formatted = feinnDateTime.getFormattedDateTime("yyyy-MM-dd HH:mm", FeinnLocale.getDefault())
 * println(formatted) // Output: "2024-11-24 12:34"
 * ```
 */
public actual fun FeinnDateTime.getFormattedDateTime(
    format: String,
    locale: FeinnLocale
): String {
    return localDateTime.toDate().getFormattedDate(format, locale.locale)
}

/**
 * Parses a string into a [FeinnDateTime] using the specified format and locale.
 *
 * @param date String - The date-time string to parse.
 * @param format String - The date-time format (e.g., "yyyy-MM-dd HH:mm:ss").
 * @param locale FeinnLocale - The locale to use for parsing.
 * @return FeinnDateTime - The parsed [FeinnDateTime] instance.
 *
 * @throws FeinnDateTimeThrowable if parsing fails.
 *
 * ### Example:
 * ```kotlin
 * val parsed = FeinnDateTime.parse("2024-11-24 12:34", "yyyy-MM-dd HH:mm", FeinnLocale.getDefault())
 * println(parsed) // Output: "2024-11-24T12:34"
 * ```
 */
public actual fun FeinnDateTime.Companion.parse(
    date: String,
    format: String,
    locale: FeinnLocale
): FeinnDateTime {
    val localDateTime = date.toDate(format, locale.locale).toLocalDateTime()
    return FeinnDateTime(localDateTime)
}

/**
 * Converts the [FeinnDateTime] instance to a [FeinnDate], retaining only the date portion.
 *
 * @return FeinnDate - The [FeinnDate] instance representing the date portion of this [FeinnDateTime].
 */
public actual fun FeinnDateTime.toFeinnDate(): FeinnDate {
    return FeinnDate(localDateTime.toLocalDate())
}

/**
 * The number of milliseconds since the epoch represented by this [FeinnDateTime].
 *
 * @return Long - The epoch milliseconds of the [localDateTime].
 */
public actual val FeinnDateTime.millisSeconds: Long
    get() = localDateTime.toDate().time
