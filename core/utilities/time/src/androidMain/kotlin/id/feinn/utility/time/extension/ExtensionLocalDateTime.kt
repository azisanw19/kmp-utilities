package id.feinn.utility.time.extension

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.ZoneId
import java.util.Date

/**
 * Converts a [LocalDateTime] instance to a [Date] object.
 *
 * This function applies the system's default time zone ([ZoneId.systemDefault]) to the
 * [LocalDateTime] to create a [ZonedDateTime], then converts it to a [Date].
 *
 * @return The equivalent [Date] representation of this [LocalDateTime].
 *
 * @sample
 * val localDateTime = LocalDateTime.now()
 * val date = localDateTime.toDate()
 * println("Date: $date")
 */
internal fun LocalDateTime.toDate() : Date {
    val zonedDateTime = this.atZone(ZoneId.systemDefault())
    return Date.from(zonedDateTime.toInstant())
}