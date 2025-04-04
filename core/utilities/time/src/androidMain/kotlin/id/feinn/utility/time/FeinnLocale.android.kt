package id.feinn.utility.time

import java.util.Locale

/**
 * A wrapper class for [Locale] that provides additional functionality and customization.
 *
 * The `FeinnLocale` class encapsulates a [Locale] instance, enabling locale-specific operations
 * such as date formatting or string localization in a consistent and platform-agnostic way.
 *
 * @constructor Creates a `FeinnLocale` instance with a default or specified [Locale].
 * - The primary constructor initializes the instance with the system's default locale ([Locale.getDefault()]).
 * - A secondary constructor allows initialization with a specified [Locale].
 *
 * @property locale Locale - The encapsulated [Locale] instance. It is initialized to the system default by default.
 *
 * ### Example Usage:
 * ```kotlin
 * // Create a FeinnLocale with the system default locale
 * val defaultLocale = FeinnLocale()
 * println(defaultLocale) // Output: "en_US" (or the system's default locale)
 *
 * // Create a FeinnLocale with a specific locale
 * val specificLocale = FeinnLocale(Locale.FRENCH)
 * println(specificLocale) // Output: "fr"
 *
 * // Modify the locale property
 * defaultLocale.locale = Locale.GERMAN
 * println(defaultLocale) // Output: "de"
 * ```
 *
 * ### Note:
 * - The `locale` property must be explicitly set if not using the default locale.
 * - Be cautious when modifying the `locale` property as it affects all operations using this instance.
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
public actual class FeinnLocale() {

    /**
     * Companion object for static methods or properties related to [FeinnLocale].
     */
    public actual companion object {}

    /**
     * Secondary constructor to initialize the [FeinnLocale] with a specified [Locale].
     *
     * @param locale The [Locale] to encapsulate.
     */
    public constructor(locale: Locale) : this() {
        this.locale = locale
    }

    /**
     * The encapsulated [Locale] instance, defaulting to the system's default locale.
     */
    public var locale: Locale = Locale.getDefault()

    /**
     * Returns the string representation of the encapsulated [Locale].
     *
     * @return String - The string representation of the [locale], typically in the format "en_US".
     */
    override fun toString(): String {
        return locale.toString()
    }

    /**
     * Returns the hash code of the encapsulated [Locale].
     *
     * @return Int - The hash code of the [locale].
     */
    override fun hashCode(): Int {
        return locale.hashCode()
    }

    /**
     * Checks equality between this [FeinnLocale] and another object.
     *
     * Two [FeinnLocale] instances are considered equal if their [locale] properties are equal.
     *
     * @param other Any? - The object to compare with.
     * @return Boolean - `true` if the other object is a [FeinnLocale] with an equal [locale], otherwise `false`.
     */
    override fun equals(other: Any?): Boolean {
        return locale == (other as? FeinnLocale)?.locale
    }
}


/**
 * Creates a new instance of [FeinnLocale] initialized with the system's default locale.
 *
 * @receiver FeinnLocale.Companion - The companion object of [FeinnLocale].
 * @return FeinnLocale - A new [FeinnLocale] instance with [FeinnLocale.locale] set to the system's default locale
 *                       ([Locale.getDefault()]).
 *
 * This method provides a convenient way to create a `FeinnLocale` object based on the default locale
 * of the system, which is commonly used in applications to handle locale-specific operations.
 *
 * Example usage:
 * ```
 * val defaultLocale = FeinnLocale.getDefault()
 * println(defaultLocale.locale) // Output: The default system locale, e.g., "en_US"
 * ```
 *
 * Note: This method is useful when you need to work with locale-specific formatting or processing
 * based on the system's current locale settings.
 */
public actual fun FeinnLocale.Companion.getDefault(): FeinnLocale {
    return FeinnLocale()
}

/**
 * Creates a [FeinnLocale] instance from a given region ID.
 *
 * This function uses the provided `regionId` to build a [Locale] object and wraps it in a [FeinnLocale].
 * Region IDs are typically two-letter ISO 3166-1 alpha-2 codes (e.g., "US", "FR", "JP").
 *
 * @param regionId String - The region ID to build the [Locale] instance from.
 * @return FeinnLocale - A [FeinnLocale] instance encapsulating the generated [Locale].
 *
 * ### Example Usage:
 * ```kotlin
 * // Create a FeinnLocale for the United States
 * val usLocale = FeinnLocale.getFromRegionId("US")
 * println(usLocale) // Output: "en_US"
 *
 * // Create a FeinnLocale for France
 * val frLocale = FeinnLocale.getFromRegionId("FR")
 * println(frLocale) // Output: "fr_FR"
 *
 * // Create a FeinnLocale for Japan
 * val jpLocale = FeinnLocale.getFromRegionId("JP")
 * println(jpLocale) // Output: "ja_JP"
 * ```
 *
 * ### Note:
 * - If the `regionId` is invalid, the resulting [Locale] may not behave as expected.
 * - Ensure that `regionId` adheres to the ISO 3166-1 alpha-2 standard for compatibility.
 */
public actual fun FeinnLocale.Companion.getFromRegionId(regionId: String): FeinnLocale {
    val locale = Locale.Builder()
        .setRegion(regionId)
        .build()
    return FeinnLocale(locale)
}
