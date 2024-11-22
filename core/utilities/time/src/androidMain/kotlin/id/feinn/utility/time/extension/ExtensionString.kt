package id.feinn.utility.time.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun String.toDate(
    format: String,
    locale: Locale = Locale.getDefault()
) : Date {
    return SimpleDateFormat(format, locale).parse(this)!!
}