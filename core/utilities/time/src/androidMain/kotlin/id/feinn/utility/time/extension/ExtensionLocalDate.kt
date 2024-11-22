package id.feinn.utility.time.extension

import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

internal fun LocalDate.toDate() : Date {
    return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
}