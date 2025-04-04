package id.feinn.utility.time.extension

import id.feinn.utility.time.exception.FeinnDateTimeThrowable
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate

/**
 * Converts the current [NSDate] to the start of the day (midnight) in the same time zone.
 *
 * This function uses the current calendar to extract the year, month, and day components
 * of the [NSDate] and sets the time components (hour, minute, second, and nanosecond) to zero,
 * effectively converting the date-time to the start of the day.
 *
 * @return NSDate - The new [NSDate] object representing the start of the day (midnight).
 * @throws FeinnDateTimeThrowable if the date conversion fails (i.e., if the calendar can't generate a valid date).
 *
 * ### Example Usage:
 * ```kotlin
 * val now = NSDate.now()
 * val startOfDay = now.toStartDay()
 * println(startOfDay) // Output: NSDate representing 00:00:00 of the current date
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
@Throws(FeinnDateTimeThrowable::class)
internal fun NSDate.toStartDay(): NSDate {
    val calendar = NSCalendar.currentCalendar
    val components = calendar.components(
        NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
        this
    )
    components.hour = 0
    components.minute = 0
    components.second = 0
    components.nanosecond = 0
    return calendar.dateFromComponents(components) ?: throw FeinnDateTimeThrowable("Failed to get start date")
}