package id.feinn.utility.time

import id.feinn.utility.time.extension.getFormattedDate
import id.feinn.utility.time.extension.toDate
import id.feinn.utility.time.extension.toLocalDate
import java.time.LocalDate

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
    public lateinit var localDate: LocalDate

    override fun toString(): String {
        return localDate.toString()
    }

    override fun hashCode(): Int {
        return localDate.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return localDate == (other as? FeinnDate)?.localDate
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
    feinnDate.localDate = LocalDate.now()
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
    return this.localDate.toDate().getFormattedDate(format, locale.locale)
}

public actual fun FeinnDate.Companion.parse(
    date: String,
    format: String,
    locale: FeinnLocale
): FeinnDate {
    val localDate = date.toDate(format, locale.locale).toLocalDate()
    val feinnDate = FeinnDate()
    feinnDate.localDate = localDate
    return feinnDate
}