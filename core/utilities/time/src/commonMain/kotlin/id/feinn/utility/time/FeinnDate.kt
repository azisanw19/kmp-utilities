package id.feinn.utility.time

/**
 * An expected class for representing a date in the [FeinnDate] system.
 *
 * The `FeinnDate` class is intended to be implemented in platform-specific code and serves as an abstraction
 * for date handling across different platforms. It can be used to wrap platform-specific date implementations,
 * ensuring consistent functionality on all supported platforms.
 *
 * Since this is an expected class, its actual implementation will vary depending on the target platform.
 *
 * Example usage:
 * ```
 * val feinnDate = FeinnDate()
 * // Platform-specific functionality will be defined in the actual implementation
 * ```
 *
 * The companion object is available for static functionality related to [FeinnDate], but the exact behavior
 * will depend on the platform's actual implementation.
 */
public expect class FeinnDate {

    /**
     * Companion object for [FeinnDate], which may contain platform-specific static methods or properties.
     */
    public companion object

}

/**
 * Creates a new instance of [FeinnDate] representing the current date.
 *
 * This function is expected to be implemented in platform-specific code to return the current date
 * in a platform-specific manner. The exact behavior of how the current date is obtained will depend
 * on the target platform.
 *
 * @receiver FeinnDate.Companion - The companion object of [FeinnDate].
 * @return FeinnDate - A new instance of [FeinnDate] initialized to the current date.
 *
 * Example usage:
 * ```
 * val today = FeinnDate.now()
 * println(today) // Output: Current date based on platform-specific implementation
 * ```
 *
 * Note:
 * - This method should return the current date according to the platform's system or locale settings.
 * - The exact implementation of obtaining the current date will vary across platforms.
 */
public expect fun FeinnDate.Companion.now(): FeinnDate

/**
 * Formats the current [FeinnDate] instance into a string according to the specified format and locale.
 *
 * This function allows you to represent a [FeinnDate] object as a string by formatting it using a specified
 * date format pattern and locale. The default format is `ISO_LOCAL_DATE` (i.e., "yyyy-MM-dd"), and the default
 * locale is the system's default locale.
 *
 * @receiver FeinnDate - The current instance of [FeinnDate] that will be formatted.
 * @param format String - The format pattern to use for the date. The default value is [FeinnDateTimeFormatter.ISO_LOCAL_DATE].
 *                       You can provide custom format patterns such as "yyyy-MM-dd", "dd/MM/yyyy", etc.
 * @param locale FeinnLocale - The locale to use for formatting. The default value is [FeinnLocale.getDefault()],
 *                             which will use the system's default locale.
 * @return String - The formatted date as a string.
 *
 * Example usage:
 * ```
 * val feinnDate = FeinnDate.now()
 * val formattedDate = feinnDate.getFormattedDate("dd-MM-yyyy", FeinnLocale.getDefault())
 * println(formattedDate) // Output: "23-11-2024"
 * ```
 *
 * Note:
 * - The `format` parameter allows you to specify any valid date format string, as supported by the underlying
 *   date formatting logic.
 * - The `locale` parameter ensures that the date is formatted according to locale-specific rules, such as month names.
 */
public expect fun FeinnDate.getFormattedDate(
    format: String = FeinnDateTimeFormatter.ISO_LOCAL_DATE,
    locale: FeinnLocale = FeinnLocale.getDefault()
): String


/**
 * Parses a date string into a [FeinnDate] object based on the specified format and locale.
 *
 * This function allows you to convert a string representation of a date into a [FeinnDate] object by parsing it
 * using the given format pattern and locale. The default format is `ISO_LOCAL_DATE` (i.e., "yyyy-MM-dd"), and the
 * default locale is the system's default locale.
 *
 * @receiver FeinnDate.Companion - The companion object of [FeinnDate], used for calling this function.
 * @param date String - The string representation of the date to be parsed. It must match the provided format.
 * @param format String - The expected date format. The default value is [FeinnDateTimeFormatter.ISO_LOCAL_DATE].
 *                       Example: "yyyy-MM-dd", "dd/MM/yyyy".
 * @param locale FeinnLocale - The locale to use for parsing the date. The default value is [FeinnLocale.getDefault()].
 *                             The locale affects parsing rules for month names, day names, etc.
 * @return FeinnDate - A new [FeinnDate] instance initialized with the parsed date.
 *
 * Example usage:
 * ```
 * val dateString = "23-11-2024"
 * val feinnDate = FeinnDate.parse(dateString, "dd-MM-yyyy", FeinnLocale.getDefault())
 * println(feinnDate) // Output: FeinnDate object representing "2024-11-23"
 * ```
 *
 * Note:
 * - The input date string must match the specified `format` exactly for successful parsing.
 * - The `locale` parameter ensures that date parsing is done according to the specified locale's rules.
 */
public expect fun FeinnDate.Companion.parse(
    date: String,
    format: String = FeinnDateTimeFormatter.ISO_LOCAL_DATE,
    locale: FeinnLocale = FeinnLocale.getDefault()
): FeinnDate

/**
 * Provides the number of milliseconds since the Unix epoch (January 1, 1970, 00:00:00 UTC)
 * for the date represented by this [FeinnDate].
 *
 * This property calculates the epoch time in milliseconds for the encapsulated date, which can be
 * useful for time-based computations, comparisons, or conversions.
 *
 * @return Long - The epoch time in milliseconds for this [FeinnDate].
 *
 * ### Example Usage:
 * ```kotlin
 * val feinnDate = FeinnDate()
 * println(feinnDate.milliseconds) // Output: 1732406400000 (example timestamp)
 *
 * val specificDate = FeinnDate(LocalDate.of(2025, 1, 1))
 * println(specificDate.milliseconds) // Output: 1735680000000
 * ```
 *
 * ### Note:
 * - The value is timezone-independent because it assumes the start of the day in the local timezone.
 * - Ensure consistent timezone handling when using this property across different platforms.
 */
public expect val FeinnDate.milliseconds: Long
