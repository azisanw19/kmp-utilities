package id.feinn.utility.time.extension

import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

/**
 * Converts a [LocalDate] object to a [Date] at the start of the day in the system's default time zone.
 *
 * @receiver LocalDate - The [LocalDate] instance to be converted.
 * @return Date - The equivalent [Date] representation of the given [LocalDate], set to the start of the day
 *                (00:00:00.000) in the system's default time zone.
 *
 * This method is useful for converting modern [java.time.LocalDate] objects into legacy [Date] objects
 * for compatibility with older APIs.
 *
 * Example usage:
 * ```
 * val localDate = LocalDate.of(2024, 11, 23)
 * val date = localDate.toDate()
 * println(date) // e.g., "Sat Nov 23 00:00:00 UTC 2024"
 * ```
 *
 * Note: The time component of the resulting [Date] will always be set to midnight (start of the day).
 */
internal fun LocalDate.toDate() : Date {
    return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
}