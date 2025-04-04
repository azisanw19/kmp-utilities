package id.feinn.utility.time.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Parses a [String] into a [Date] object based on the specified format and locale.
 *
 * @receiver String - The string representation of the date to be parsed.
 * @param format String - The expected format of the date string, following the [SimpleDateFormat] pattern.
 *                        Examples: "yyyy-MM-dd", "dd/MM/yyyy", "MMM dd, yyyy".
 * @param locale Locale - The locale to be used for parsing, which affects elements like month and day names.
 *                        Defaults to the system's default locale ([Locale.getDefault()]).
 * @return Date - The [Date] object representing the parsed date.
 *
 * @throws ParseException if the string cannot be parsed into a date with the given format.
 * @throws NullPointerException if the parsing result is null (use with caution as `!!` is used).
 *
 * Example usage:
 * ```
 * val dateString = "23-11-2024"
 * val date = dateString.toDate("dd-MM-yyyy")
 * println(date) // e.g., "Sat Nov 23 00:00:00 UTC 2024"
 *
 * val dateWithLocale = "23 November 2024"
 * val dateWithLocaleParsed = dateWithLocale.toDate("dd MMMM yyyy", Locale.ENGLISH)
 * println(dateWithLocaleParsed) // e.g., "Sat Nov 23 00:00:00 UTC 2024"
 * ```
 *
 * Note: Ensure the input string matches the format exactly; otherwise, a [ParseException] will be thrown.
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
internal fun String.toDate(
    format: String,
    locale: Locale = Locale.getDefault()
) : Date {
    return SimpleDateFormat(format, locale).parse(this)!!
}