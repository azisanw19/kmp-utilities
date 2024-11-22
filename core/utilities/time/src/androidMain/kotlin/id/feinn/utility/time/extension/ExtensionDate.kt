package id.feinn.utility.time.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Returns the formatted date string based on the specified format and locale.
 *
 * @param format The format string to use. Default is "dd MMMM yyyy".
 * @param locale The locale to use for formatting. Default is Locale("id", "ID").
 * @return The formatted date string.
 */
internal fun Date.getFormattedDate(
    format: String,
    locale: Locale
) : String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}