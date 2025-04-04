package id.feinn.utility.time

/**
 * A utility object that provides commonly used date and time format patterns based on the ISO 8601 standard.
 *
 * This object contains constants for the most common date and date-time formats:
 * - [ISO_LOCAL_DATE] for date in the format "yyyy-MM-dd".
 * - [ISO_LOCAL_DATE_TIME] for date and time in the format "yyyy-MM-dd'T'HH:mm:ss".
 *
 * These constants can be used to format or parse dates and times according to the ISO 8601 standard.
 *
 * ISO 8601 is an international standard covering the exchange of date- and time-related data.
 * It is widely used in computing and data interchange formats.
 *
 * Example usage:
 * ```
 * val dateFormatter = SimpleDateFormat(FeinnDateTimeFormatter.ISO_LOCAL_DATE)
 * val formattedDate = dateFormatter.format(LocalDate.now())
 * println(formattedDate) // Output: "2024-11-23"
 *
 * val dateTimeFormatter = SimpleDateFormat(FeinnDateTimeFormatter.ISO_LOCAL_DATE_TIME)
 * val formattedDateTime = dateTimeFormatter.format(LocalDateTime.now())
 * println(formattedDateTime) // Output: "2024-11-23T15:45:30"
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
public object FeinnDateTimeFormatter {
    /**
     * The ISO 8601 format for a date without time: "yyyy-MM-dd".
     * Example: "2024-11-23"
     */
    public const val ISO_LOCAL_DATE: String = "yyyy-MM-dd"

    /**
     * The ISO 8601 format for a date and time: "yyyy-MM-dd'T'HH:mm:ss".
     * Example: "2024-11-23T15:45:30"
     */
    public const val ISO_LOCAL_DATE_TIME: String = "yyyy-MM-dd'T'HH:mm:ss"
}
