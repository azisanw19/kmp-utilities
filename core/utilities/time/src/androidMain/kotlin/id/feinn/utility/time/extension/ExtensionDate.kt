package id.feinn.utility.time.extension

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

/**
 * Formats a [Date] object into a string representation based on the specified format and locale.
 *
 * @receiver Date - The date instance to be formatted.
 * @param format String - The desired format for the date, following the [SimpleDateFormat] pattern.
 *                        Examples: "yyyy-MM-dd", "dd MMMM yyyy", "hh:mm a".
 * @param locale Locale - The locale to be used for formatting the date, which affects elements
 *                        like month and day names.
 * @return String - The formatted date as a string.
 *
 * @throws IllegalArgumentException if the format string is invalid or not recognized by [SimpleDateFormat].
 *
 * Example usage:
 * ```
 * val date = Date()
 * val formattedDate = date.getFormattedDate("dd MMM yyyy", Locale.ENGLISH)
 * println(formattedDate) // e.g., "23 Nov 2024"
 * ```
 */
internal fun Date.getFormattedDate(
    format: String,
    locale: Locale
) : String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

/**
 * Converts a [Date] object to a [LocalDate] using the specified [ZoneId].
 *
 * @receiver Date - The date instance to be converted.
 * @param zoneId ZoneId - The time zone to be used for the conversion. Defaults to the system's default time zone
 *                        ([ZoneId.systemDefault()]) if not specified.
 * @return LocalDate - The [LocalDate] representation of the given [Date].
 *
 * This method is useful for converting legacy [Date] objects into the modern [java.time.LocalDate]
 * format introduced in Java 8.
 *
 * Example usage:
 * ```
 * val date = Date()
 * val localDate = date.toLocalDate()
 * println(localDate) // e.g., "2024-11-23"
 *
 * val specificZone = ZoneId.of("America/New_York")
 * val localDateInZone = date.toLocalDate(specificZone)
 * println(localDateInZone) // e.g., "2024-11-22"
 * ```
 */
internal fun Date.toLocalDate(
    zoneId: ZoneId = ZoneId.systemDefault()
): LocalDate {
    return this.toInstant()
        .atZone(zoneId)
        .toLocalDate()
}

/**
 * Converts a [Date] instance to a [LocalDateTime] object, adjusted to the specified [ZoneId].
 *
 * This function uses the system's default time zone ([ZoneId.systemDefault]) unless another
 * [ZoneId] is provided. The conversion is achieved by transforming the [Date] into an
 * [Instant], applying the zone offset, and then extracting the local date-time.
 *
 * @param zoneId The time zone to apply during the conversion. Defaults to the system's default zone.
 * @return The equivalent [LocalDateTime] representation of this [Date] in the specified time zone.
 *
 * @sample
 * val date = Date()
 * val localDateTime = date.toLocalDateTime()
 * println("LocalDateTime: $localDateTime")
 */
internal fun Date.toLocalDateTime(
    zoneId: ZoneId = ZoneId.systemDefault()
): LocalDateTime {
    return this.toInstant()
        .atZone(zoneId)
        .toLocalDateTime()
}