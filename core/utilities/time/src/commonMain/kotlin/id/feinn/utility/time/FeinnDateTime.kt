package id.feinn.utility.time

import id.feinn.utility.time.exception.FeinnDateTimeThrowable

/**
 * Represents a date and time with platform-specific implementations for different environments.
 *
 * The `FeinnDateTime` class encapsulates a date-time instance and provides methods for creating,
 * formatting, parsing, and converting date-time objects in a platform-agnostic way.
 */
public expect class FeinnDateTime {

    /**
     * Companion object to hold static methods or properties related to [FeinnDateTime].
     */
    public companion object
}

/**
 * Retrieves the current date and time as a [FeinnDateTime] instance.
 *
 * @return FeinnDateTime - The current date and time.
 *
 * ### Example Usage:
 * ```kotlin
 * val now = FeinnDateTime.now()
 * println(now) // Output: "2024-11-24T14:30:00" (example value)
 * ```
 */
public expect fun FeinnDateTime.Companion.now(): FeinnDateTime

/**
 * Formats the current [FeinnDateTime] instance into a string based on the provided format and locale.
 *
 * @param format String - The desired date-time format (e.g., "yyyy-MM-dd'T'HH:mm:ss").
 * @param locale FeinnLocale - The locale to use for formatting (defaults to [FeinnLocale.getDefault()]).
 * @return String - The formatted date-time string.
 *
 * ### Example Usage:
 * ```kotlin
 * val dateTime = FeinnDateTime.now()
 * val formatted = dateTime.getFormattedDateTime("yyyy-MM-dd HH:mm:ss", FeinnLocale.getDefault())
 * println(formatted) // Output: "2024-11-24 14:30:00" (example value)
 * ```
 */

public expect fun FeinnDateTime.getFormattedDateTime(
    format: String,
    locale: FeinnLocale = FeinnLocale.getDefault()
): String

/**
 * Parses a string representation of a date-time into a [FeinnDateTime] instance.
 *
 * @param date String - The date-time string to parse (e.g., "2024-11-24T14:30:00").
 * @param format String - The format of the date-time string (default: ISO_LOCAL_DATE_TIME).
 * @param locale FeinnLocale - The locale to use for parsing (defaults to [FeinnLocale.getDefault()]).
 * @return FeinnDateTime - The parsed date-time instance.
 * @throws FeinnDateTimeThrowable if parsing fails.
 *
 * ### Example Usage:
 * ```kotlin
 * val dateTimeString = "2024-11-24T14:30:00"
 * val dateTime = FeinnDateTime.parse(dateTimeString)
 * println(dateTime) // Output: FeinnDateTime object
 * ```
 */
public expect fun FeinnDateTime.Companion.parse(
    date: String,
    format: String = FeinnDateTimeFormatter.ISO_LOCAL_DATE_TIME,
    locale: FeinnLocale = FeinnLocale.getDefault()
): FeinnDateTime

/**
 * Converts this [FeinnDateTime] instance into a [FeinnDate], retaining only the date portion.
 *
 * @return FeinnDate - The date portion of the current [FeinnDateTime].
 *
 * ### Example Usage:
 * ```kotlin
 * val dateTime = FeinnDateTime.now()
 * val date = dateTime.toFeinnDate()
 * println(date) // Output: FeinnDate object representing the date
 * ```
 */
public expect fun FeinnDateTime.toFeinnDate(): FeinnDate

/**
 * Retrieves the number of milliseconds since the Unix epoch (January 1, 1970, 00:00:00 UTC)
 * for this [FeinnDateTime].
 *
 * @return Long - The epoch time in milliseconds.
 *
 * ### Example Usage:
 * ```kotlin
 * val dateTime = FeinnDateTime.now()
 * println(dateTime.millisSeconds) // Output: 1732406400000 (example timestamp)
 * ```
 */
public expect val FeinnDateTime.millisSeconds: Long
