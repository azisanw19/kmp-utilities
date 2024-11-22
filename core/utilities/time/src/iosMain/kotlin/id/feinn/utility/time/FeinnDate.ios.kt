package id.feinn.utility.time

import id.feinn.utility.time.exception.FeinnDateTimeThrowable
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.now

/**
 * A date without a time-zone in the ISO-8601 calendar system,
 * such as {@code 2007-12-03}.
 * <p>
 * {@code LocalDate} is an immutable date-time object that represents a date,
 * often viewed as year-month-day. Other date fields, such as day-of-year,
 * day-of-week and week-of-year, can also be accessed.
 * For example, the value "2nd October 2007" can be stored in a {@code LocalDate}.
 * <p>
 * This class does not store or represent a time or time-zone.
 * Instead, it is a description of the date, as used for birthdays.
 * It cannot represent an instant on the time-line without additional information
 * such as an offset or time-zone.
 * <p>
 * The ISO-8601 calendar system is the modern civil calendar system used today
 * in most of the world. It is equivalent to the proleptic Gregorian calendar
 * system, in which today's rules for leap years are applied for all time.
 * For most applications written today, the ISO-8601 rules are entirely suitable.
 * However, any application that makes use of historical dates, and requires them
 * to be accurate will find the ISO-8601 approach unsuitable.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
public actual class FeinnDate {

    public actual companion object {}
    public lateinit var nsDate: NSDate

    override fun toString(): String {
        return nsDate.toString()
    }

    override fun hashCode(): Int {
        return nsDate.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return nsDate == (other as? FeinnDate)?.nsDate
    }

}

/**
 * Obtains the current date from the system clock in the default time-zone.
 * <p>
 * This will query the {@link Clock#systemDefaultZone() system clock} in the default
 * time-zone to obtain the current date.
 * <p>
 * Using this method will prevent the ability to use an alternate clock for testing
 * because the clock is hard-coded.
 *
 * @return the current date using the system clock and default time-zone, not null
 */
public actual fun FeinnDate.Companion.now(): FeinnDate {
    val feinnDate = FeinnDate()

    val nsDate = NSDate.now()
    feinnDate.nsDate = nsDate
    return feinnDate
}

/**
 * Outputs this date as a {@code String}, such as {@code 2007-12-03}.
 * <p>
 * The output will be in the ISO-8601 format {@code uuuu-MM-dd}.
 *
 * @return a string representation of this date, not null
 */
public actual fun FeinnDate.getFormattedDate(
    format: String,
    locale: FeinnLocale
): String {
    val nsDateFormatter = NSDateFormatter()
    nsDateFormatter.dateFormat = format
    nsDateFormatter.locale = locale.locale
    return nsDateFormatter.stringFromDate(this.nsDate)
}

@Throws(FeinnDateTimeThrowable::class)
public actual fun FeinnDate.Companion.parse(
    date: String,
    format: String,
    locale: FeinnLocale
): FeinnDate {
    val dateFormatter = NSDateFormatter()
    dateFormatter.dateFormat = format
    dateFormatter.locale = locale.locale
    val nsDate = dateFormatter.dateFromString(date) ?: throw FeinnDateTimeThrowable("Failed to parse date")
    val feinnDate = FeinnDate()
    feinnDate.nsDate = nsDate
    return feinnDate
}