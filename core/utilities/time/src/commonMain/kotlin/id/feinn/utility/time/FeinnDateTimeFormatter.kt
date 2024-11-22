package id.feinn.utility.time

/**
 * Formatter for printing and parsing date-time objects.
 * <p>
 * This class provides the main application entry point for printing and parsing
 * and provides common implementations of {@code DateTimeFormatter}:
 * <ul>
 * <li>Using predefined constants, such as {@link #ISO_LOCAL_DATE}</li>
 * <li>Using pattern letters, such as {@code uuuu-MMM-dd}</li>
 * <li>Using localized styles, such as {@code long} or {@code medium}</li>
 * </ul>
 * <p>
 * More complex formatters are provided by
 * {@link DateTimeFormatterBuilder DateTimeFormatterBuilder}.
 *
 * <p>
 * The main date-time classes provide two methods - one for formatting,
 * {@code format(DateTimeFormatter formatter)}, and one for parsing,
 * {@code parse(CharSequence text, DateTimeFormatter formatter)}.
 * <p>For example:
 * <blockquote><pre>
 *  LocalDate date = LocalDate.now();
 *  String text = date.format(formatter);
 *  LocalDate parsedDate = LocalDate.parse(text, formatter);
 * </pre></blockquote>
 * <p>
 * In addition to the format, formatters can be created with desired Locale,
 * Chronology, ZoneId, and DecimalStyle.
 * <p>
 * The {@link #withLocale withLocale} method returns a new formatter that
 * overrides the locale. The locale affects some aspects of formatting and
 * parsing. For example, the {@link #ofLocalizedDate ofLocalizedDate} provides a
 * formatter that uses the locale specific date format.
 * <p>
 * The {@link #withChronology withChronology} method returns a new formatter
 * that overrides the chronology. If overridden, the date-time value is
 * converted to the chronology before formatting. During parsing the date-time
 * value is converted to the chronology before it is returned.
 * <p>
 * The {@link #withZone withZone} method returns a new formatter that overrides
 * the zone. If overridden, the date-time value is converted to a ZonedDateTime
 * with the requested ZoneId before formatting. During parsing the ZoneId is
 * applied before the value is returned.
 * <p>
 * The {@link #withDecimalStyle withDecimalStyle} method returns a new formatter that
 * overrides the {@link DecimalStyle}. The DecimalStyle symbols are used for
 * formatting and parsing.
 * <p>
 * Some applications may need to use the older {@link Format java.text.Format}
 * class for formatting. The {@link #toFormat()} method returns an
 * implementation of {@code java.text.Format}.
 *
 * <h2 id="predefined">Predefined Formatters</h2>
 * <table class="striped" style="text-align:left">
 * <caption>Predefined Formatters</caption>
 * <thead>
 * <tr>
 * <th scope="col">Formatter</th>
 * <th scope="col">Description</th>
 * <th scope="col">Example</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <th scope="row">{@link #ofLocalizedDate ofLocalizedDate(dateStyle)} </th>
 * <td> Formatter with date style from the locale </td>
 * <td> '2011-12-03'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #ofLocalizedTime ofLocalizedTime(timeStyle)} </th>
 * <td> Formatter with time style from the locale </td>
 * <td> '10:15:30'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #ofLocalizedDateTime ofLocalizedDateTime(dateTimeStyle)} </th>
 * <td> Formatter with a style for date and time from the locale</td>
 * <td> '3 Jun 2008 11:05:30'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #ofLocalizedDateTime ofLocalizedDateTime(dateStyle,timeStyle)}
 * </th>
 * <td> Formatter with date and time styles from the locale </td>
 * <td> '3 Jun 2008 11:05'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #BASIC_ISO_DATE}</th>
 * <td>Basic ISO date </td> <td>'20111203'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #ISO_LOCAL_DATE}</th>
 * <td> ISO Local Date </td>
 * <td>'2011-12-03'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #ISO_OFFSET_DATE}</th>
 * <td> ISO Date with offset </td>
 * <td>'2011-12-03+01:00'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #ISO_DATE}</th>
 * <td> ISO Date with or without offset </td>
 * <td> '2011-12-03+01:00'; '2011-12-03'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #ISO_LOCAL_TIME}</th>
 * <td> Time without offset </td>
 * <td>'10:15:30'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #ISO_OFFSET_TIME}</th>
 * <td> Time with offset </td>
 * <td>'10:15:30+01:00'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #ISO_TIME}</th>
 * <td> Time with or without offset </td>
 * <td>'10:15:30+01:00'; '10:15:30'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #ISO_LOCAL_DATE_TIME}</th>
 * <td> ISO Local Date and Time </td>
 * <td>'2011-12-03T10:15:30'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #ISO_OFFSET_DATE_TIME}</th>
 * <td> Date Time with Offset
 * </td><td>'2011-12-03T10:15:30+01:00'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #ISO_ZONED_DATE_TIME}</th>
 * <td> Zoned Date Time </td>
 * <td>'2011-12-03T10:15:30+01:00[Europe/Paris]'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #ISO_DATE_TIME}</th>
 * <td> Date and time with ZoneId </td>
 * <td>'2011-12-03T10:15:30+01:00[Europe/Paris]'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #ISO_ORDINAL_DATE}</th>
 * <td> Year and day of year </td>
 * <td>'2012-337'</td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #ISO_WEEK_DATE}</th>
 * <td> Year and Week </td>
 * <td>'2012-W48-6'</td></tr>
 * <tr>
 * <th scope="row"> {@link #ISO_INSTANT}</th>
 * <td> Date and Time of an Instant </td>
 * <td>'2011-12-03T10:15:30Z' </td>
 * </tr>
 * <tr>
 * <th scope="row"> {@link #RFC_1123_DATE_TIME}</th>
 * <td> RFC 1123 / RFC 822 </td>
 * <td>'Tue, 3 Jun 2008 11:05:30 GMT'</td>
 * </tr>
 * </tbody>
 * </table>
 *
 * <h2 id="patterns">Patterns for Formatting and Parsing</h2>
 * Patterns are based on a simple sequence of letters and symbols.
 * A pattern is used to create a Formatter using the
 * {@link #ofPattern(String)} and {@link #ofPattern(String, Locale)} methods.
 * For example,
 * {@code "d MMM uuuu"} will format 2011-12-03 as '3&nbsp;Dec&nbsp;2011'.
 * A formatter created from a pattern can be used as many times as necessary,
 * it is immutable and is thread-safe.
 * <p>
 * For example:
 * <blockquote><pre>
 *  LocalDate date = LocalDate.now();
 *  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd");
 *  String text = date.format(formatter);
 *  LocalDate parsedDate = LocalDate.parse(text, formatter);
 * </pre></blockquote>
 * <p>
 * All letters 'A' to 'Z' and 'a' to 'z' are reserved as pattern letters. The
 * following pattern letters are defined:
 * <table class="striped">
 * <caption>Pattern Letters and Symbols</caption>
 * <thead>
 *  <tr><th scope="col">Symbol</th>   <th scope="col">Meaning</th>         <th scope="col">Presentation</th> <th scope="col">Examples</th>
 * </thead>
 * <tbody>
 *   <tr><th scope="row">G</th>       <td>era</td>                         <td>text</td>              <td>AD; Anno Domini; A</td>
 *   <tr><th scope="row">u</th>       <td>year</td>                        <td>year</td>              <td>2004; 04</td>
 *   <tr><th scope="row">y</th>       <td>year-of-era</td>                 <td>year</td>              <td>2004; 04</td>
 *   <tr><th scope="row">D</th>       <td>day-of-year</td>                 <td>number</td>            <td>189</td>
 *   <tr><th scope="row">M/L</th>     <td>month-of-year</td>               <td>number/text</td>       <td>7; 07; Jul; July; J</td>
 *   <tr><th scope="row">d</th>       <td>day-of-month</td>                <td>number</td>            <td>10</td>
 *   <tr><th scope="row">g</th>       <td>modified-julian-day</td>         <td>number</td>            <td>2451334</td>
 *
 *   <tr><th scope="row">Q/q</th>     <td>quarter-of-year</td>             <td>number/text</td>       <td>3; 03; Q3; 3rd quarter</td>
 *   <tr><th scope="row">Y</th>       <td>week-based-year</td>             <td>year</td>              <td>1996; 96</td>
 *   <tr><th scope="row">w</th>       <td>week-of-week-based-year</td>     <td>number</td>            <td>27</td>
 *   <tr><th scope="row">W</th>       <td>week-of-month</td>               <td>number</td>            <td>4</td>
 *   <tr><th scope="row">E</th>       <td>day-of-week</td>                 <td>text</td>              <td>Tue; Tuesday; T</td>
 *   <tr><th scope="row">e/c</th>     <td>localized day-of-week</td>       <td>number/text</td>       <td>2; 02; Tue; Tuesday; T</td>
 *   <tr><th scope="row">F</th>       <td>day-of-week-in-month</td>        <td>number</td>            <td>3</td>
 *
 *   <tr><th scope="row">a</th>       <td>am-pm-of-day</td>                <td>text</td>              <td>PM</td>
 *   <tr><th scope="row">h</th>       <td>clock-hour-of-am-pm (1-12)</td>  <td>number</td>            <td>12</td>
 *   <tr><th scope="row">K</th>       <td>hour-of-am-pm (0-11)</td>        <td>number</td>            <td>0</td>
 *   <tr><th scope="row">k</th>       <td>clock-hour-of-day (1-24)</td>    <td>number</td>            <td>24</td>
 *
 *   <tr><th scope="row">H</th>       <td>hour-of-day (0-23)</td>          <td>number</td>            <td>0</td>
 *   <tr><th scope="row">m</th>       <td>minute-of-hour</td>              <td>number</td>            <td>30</td>
 *   <tr><th scope="row">s</th>       <td>second-of-minute</td>            <td>number</td>            <td>55</td>
 *   <tr><th scope="row">S</th>       <td>fraction-of-second</td>          <td>fraction</td>          <td>978</td>
 *   <tr><th scope="row">A</th>       <td>milli-of-day</td>                <td>number</td>            <td>1234</td>
 *   <tr><th scope="row">n</th>       <td>nano-of-second</td>              <td>number</td>            <td>987654321</td>
 *   <tr><th scope="row">N</th>       <td>nano-of-day</td>                 <td>number</td>            <td>1234000000</td>
 *
 *   <tr><th scope="row">V</th>       <td>time-zone ID</td>                <td>zone-id</td>           <td>America/Los_Angeles; Z; -08:30</td>
 *   <tr><th scope="row">v</th>       <td>generic time-zone name</td>      <td>zone-name</td>         <td>Pacific Time; PT</td>
 *   <tr><th scope="row">z</th>       <td>time-zone name</td>              <td>zone-name</td>         <td>Pacific Standard Time; PST</td>
 *   <tr><th scope="row">O</th>       <td>localized zone-offset</td>       <td>offset-O</td>          <td>GMT+8; GMT+08:00; UTC-08:00</td>
 *   <tr><th scope="row">X</th>       <td>zone-offset 'Z' for zero</td>    <td>offset-X</td>          <td>Z; -08; -0830; -08:30; -083015; -08:30:15</td>
 *   <tr><th scope="row">x</th>       <td>zone-offset</td>                 <td>offset-x</td>          <td>+0000; -08; -0830; -08:30; -083015; -08:30:15</td>
 *   <tr><th scope="row">Z</th>       <td>zone-offset</td>                 <td>offset-Z</td>          <td>+0000; -0800; -08:00</td>
 *
 *   <tr><th scope="row">p</th>       <td>pad next</td>                    <td>pad modifier</td>      <td>1</td>
 *
 *   <tr><th scope="row">'</th>       <td>escape for text</td>             <td>delimiter</td>         <td></td>
 *   <tr><th scope="row">''</th>      <td>single quote</td>                <td>literal</td>           <td>'</td>
 *   <tr><th scope="row">[</th>       <td>optional section start</td>      <td></td>                  <td></td>
 *   <tr><th scope="row">]</th>       <td>optional section end</td>        <td></td>                  <td></td>
 *   <tr><th scope="row">#</th>       <td>reserved for future use</td>     <td></td>                  <td></td>
 *   <tr><th scope="row">{</th>       <td>reserved for future use</td>     <td></td>                  <td></td>
 *   <tr><th scope="row">}</th>       <td>reserved for future use</td>     <td></td>                  <td></td>
 * </tbody>
 * </table>
 * <p>
 * The count of pattern letters determines the format.
 * <p>
 * <b>Text</b>: The text style is determined based on the number of pattern
 * letters used. Less than 4 pattern letters will use the
 * {@link TextStyle#SHORT short form}. Exactly 4 pattern letters will use the
 * {@link TextStyle#FULL full form}. Exactly 5 pattern letters will use the
 * {@link TextStyle#NARROW narrow form}.
 * Pattern letters 'L', 'c', and 'q' specify the stand-alone form of the text styles.
 * <p>
 * <b>Number</b>: If the count of letters is one, then the value is output using
 * the minimum number of digits and without padding. Otherwise, the count of digits
 * is used as the width of the output field, with the value zero-padded as necessary.
 * The following pattern letters have constraints on the count of letters.
 * Only one letter of 'c' and 'F' can be specified.
 * Up to two letters of 'd', 'H', 'h', 'K', 'k', 'm', and 's' can be specified.
 * Up to three letters of 'D' can be specified.
 * <p>
 * <b>Number/Text</b>: If the count of pattern letters is 3 or greater, use the
 * Text rules above. Otherwise use the Number rules above.
 * <p>
 * <b>Fraction</b>: Outputs the nano-of-second field as a fraction-of-second.
 * The nano-of-second value has nine digits, thus the count of pattern letters
 * is from 1 to 9. If it is less than 9, then the nano-of-second value is
 * truncated, with only the most significant digits being output.
 * <p>
 * <b>Year</b>: The count of letters determines the minimum field width below
 * which padding is used. If the count of letters is two, then a
 * {@link DateTimeFormatterBuilder#appendValueReduced reduced} two digit form is
 * used. For printing, this outputs the rightmost two digits. For parsing, this
 * will parse using the base value of 2000, resulting in a year within the range
 * 2000 to 2099 inclusive. If the count of letters is less than four (but not
 * two), then the sign is only output for negative years as per
 * {@link SignStyle#NORMAL}. Otherwise, the sign is output if the pad width is
 * exceeded, as per {@link SignStyle#EXCEEDS_PAD}.
 * <p>
 * <b>ZoneId</b>: This outputs the time-zone ID, such as 'Europe/Paris'. If the
 * count of letters is two, then the time-zone ID is output. Any other count of
 * letters throws {@code IllegalArgumentException}.
 * <p>
 * <b>Zone names</b>: This outputs the display name of the time-zone ID. If the
 * pattern letter is 'z' the output is the daylight savings aware zone name.
 * If there is insufficient information to determine whether DST applies,
 * the name ignoring daylight savings time will be used.
 * If the count of letters is one, two or three, then the short name is output.
 * If the count of letters is four, then the full name is output.
 * Five or more letters throws {@code IllegalArgumentException}.
 * <p>
 * If the pattern letter is 'v' the output provides the zone name ignoring
 * daylight savings time. If the count of letters is one, then the short name is output.
 * If the count of letters is four, then the full name is output.
 * Two, three and five or more letters throw {@code IllegalArgumentException}.
 * <p>
 * <b>Offset X and x</b>: This formats the offset based on the number of pattern
 * letters. One letter outputs just the hour, such as '+01', unless the minute
 * is non-zero in which case the minute is also output, such as '+0130'. Two
 * letters outputs the hour and minute, without a colon, such as '+0130'. Three
 * letters outputs the hour and minute, with a colon, such as '+01:30'. Four
 * letters outputs the hour and minute and optional second, without a colon,
 * such as '+013015'. Five letters outputs the hour and minute and optional
 * second, with a colon, such as '+01:30:15'. Six or more letters throws
 * {@code IllegalArgumentException}. Pattern letter 'X' (upper case) will output
 * 'Z' when the offset to be output would be zero, whereas pattern letter 'x'
 * (lower case) will output '+00', '+0000', or '+00:00'.
 * <p>
 * <b>Offset O</b>: With a non-zero offset, this formats the localized offset
 * based on the number of pattern letters. One letter outputs the
 * {@linkplain TextStyle#SHORT short} form of the localized offset, which is
 * localized offset text, such as 'GMT', with hour without leading zero, optional
 * 2-digit minute and second if non-zero, and colon, for example 'GMT+8'. Four
 * letters outputs the {@linkplain TextStyle#FULL full} form, which is localized
 * offset text, such as 'GMT, with 2-digit hour and minute field, optional second
 * field if non-zero, and colon, for example 'GMT+08:00'. If the offset is zero,
 * only localized text is output. Any other count of letters throws
 * {@code IllegalArgumentException}.
 * <p>
 * <b>Offset Z</b>: This formats the offset based on the number of pattern
 * letters. One, two or three letters outputs the hour and minute, without a
 * colon, such as '+0130'. The output will be '+0000' when the offset is zero.
 * Four letters outputs the {@linkplain TextStyle#FULL full} form of localized
 * offset, equivalent to four letters of Offset-O. The output will be the
 * corresponding localized offset text if the offset is zero. Five
 * letters outputs the hour, minute, with optional second if non-zero, with
 * colon. It outputs 'Z' if the offset is zero.
 * Six or more letters throws {@code IllegalArgumentException}.
 * <p>
 * <b>Optional section</b>: The optional section markers work exactly like
 * calling {@link DateTimeFormatterBuilder#optionalStart()} and
 * {@link DateTimeFormatterBuilder#optionalEnd()}.
 * <p>
 * <b>Pad modifier</b>: Modifies the pattern that immediately follows to be
 * padded with spaces. The pad width is determined by the number of pattern
 * letters. This is the same as calling
 * {@link DateTimeFormatterBuilder#padNext(int)}.
 * <p>
 * For example, 'ppH' outputs the hour-of-day padded on the left with spaces to
 * a width of 2.
 * <p>
 * Any unrecognized letter is an error. Any non-letter character, other than
 * '[', ']', '{', '}', '#' and the single quote will be output directly.
 * Despite this, it is recommended to use single quotes around all characters
 * that you want to output directly to ensure that future changes do not break
 * your application.
 *
 * <h2 id="resolving">Resolving</h2>
 * Parsing is implemented as a two-phase operation.
 * First, the text is parsed using the layout defined by the formatter, producing
 * a {@code Map} of field to value, a {@code ZoneId} and a {@code Chronology}.
 * Second, the parsed data is <em>resolved</em>, by validating, combining and
 * simplifying the various fields into more useful ones.
 * <p>
 * Five parsing methods are supplied by this class.
 * Four of these perform both the parse and resolve phases.
 * The fifth method, {@link #parseUnresolved(CharSequence, ParsePosition)},
 * only performs the first phase, leaving the result unresolved.
 * As such, it is essentially a low-level operation.
 * <p>
 * The resolve phase is controlled by two parameters, set on this class.
 * <p>
 * The {@link ResolverStyle} is an enum that offers three different approaches,
 * strict, smart and lenient. The smart option is the default.
 * It can be set using {@link #withResolverStyle(ResolverStyle)}.
 * <p>
 * The {@link #withResolverFields(TemporalField...)} parameter allows the
 * set of fields that will be resolved to be filtered before resolving starts.
 * For example, if the formatter has parsed a year, month, day-of-month
 * and day-of-year, then there are two approaches to resolve a date:
 * (year + month + day-of-month) and (year + day-of-year).
 * The resolver fields allows one of the two approaches to be selected.
 * If no resolver fields are set then both approaches must result in the same date.
 * <p>
 * Resolving separate fields to form a complete date and time is a complex
 * process with behaviour distributed across a number of classes.
 * It follows these steps:
 * <ol>
 * <li>The chronology is determined.
 * The chronology of the result is either the chronology that was parsed,
 * or if no chronology was parsed, it is the chronology set on this class,
 * or if that is null, it is {@code IsoChronology}.
 * <li>The {@code ChronoField} date fields are resolved.
 * This is achieved using {@link Chronology#resolveDate(Map, ResolverStyle)}.
 * Documentation about field resolution is located in the implementation
 * of {@code Chronology}.
 * <li>The {@code ChronoField} time fields are resolved.
 * This is documented on {@link ChronoField} and is the same for all chronologies.
 * <li>Any fields that are not {@code ChronoField} are processed.
 * This is achieved using {@link TemporalField#resolve(Map, TemporalAccessor, ResolverStyle)}.
 * Documentation about field resolution is located in the implementation
 * of {@code TemporalField}.
 * <li>The {@code ChronoField} date and time fields are re-resolved.
 * This allows fields in step four to produce {@code ChronoField} values
 * and have them be processed into dates and times.
 * <li>A {@code LocalTime} is formed if there is at least an hour-of-day available.
 * This involves providing default values for minute, second and fraction of second.
 * <li>Any remaining unresolved fields are cross-checked against any
 * date and/or time that was resolved. Thus, an earlier stage would resolve
 * (year + month + day-of-month) to a date, and this stage would check that
 * day-of-week was valid for the date.
 * <li>If an {@linkplain #parsedExcessDays() excess number of days}
 * was parsed then it is added to the date if a date is available.
 * <li> If a second-based field is present, but {@code LocalTime} was not parsed,
 * then the resolver ensures that milli, micro and nano second values are
 * available to meet the contract of {@link ChronoField}.
 * These will be set to zero if missing.
 * <li>If both date and time were parsed and either an offset or zone is present,
 * the field {@link ChronoField#INSTANT_SECONDS} is created.
 * If an offset was parsed then the offset will be combined with the
 * {@code LocalDateTime} to form the instant, with any zone ignored.
 * If a {@code ZoneId} was parsed without an offset then the zone will be
 * combined with the {@code LocalDateTime} to form the instant using the rules
 * of {@link ChronoLocalDateTime#atZone(ZoneId)}.
 * </ol>
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
public object FeinnDateTimeFormatter {
    public const val ISO_DATE: String = "yyyy-MM-dd"
}