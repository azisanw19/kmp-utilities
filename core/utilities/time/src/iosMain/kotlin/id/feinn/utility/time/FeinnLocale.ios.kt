package id.feinn.utility.time

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.systemLocale

/**
 * The actual implementation of [FeinnLocale] for iOS, representing a locale using [NSLocale].
 *
 * The `FeinnLocale` class encapsulates an [NSLocale] instance from the iOS SDK, which holds locale-specific
 * information such as the language, country, and variant for system operations. This class provides
 * methods to interact with and retrieve locale information for iOS-specific use cases.
 * The `locale` property stores the actual [NSLocale] instance.
 *
 * Example usage:
 * ```
 * val currentLocale = FeinnLocale.getDefault()
 * println(currentLocale) // Output: Current system locale
 * ```
 */
public actual class FeinnLocale() {

    /**
     * The companion object for [FeinnLocale], which provides static methods related to the class.
     */
    public actual companion object {}

    /**
     * Constructor to initialize [FeinnLocale] with a custom [NSLocale].
     *
     * @param locale [NSLocale] - The [NSLocale] instance to initialize the [FeinnLocale] with.
     */
    public constructor(locale: NSLocale) : this() {
        this.locale = locale
    }

    /**
     * The actual [NSLocale] object that holds the locale information.
     *
     * This property stores the system's default locale if not explicitly set.
     */
    public var locale: NSLocale = NSLocale.currentLocale

    /**
     * Returns the string representation of the current [FeinnLocale] instance.
     *
     * This method overrides the default `toString()` to return a string representation of the underlying
     * [NSLocale] object, which typically includes information about the locale such as language, country, etc.
     *
     * @return String - The string representation of the [locale] instance.
     */
    override fun toString(): String {
        return locale.toString()
    }

    /**
     * Returns the hash code for the current [FeinnLocale] instance.
     *
     * This method overrides the default `hashCode()` to ensure correct behavior when [FeinnLocale] instances are used
     * in hash-based collections.
     *
     * @return Int - The hash code of the [locale] instance.
     */
    override fun hashCode(): Int {
        return locale.hashCode()
    }

    /**
     * Compares this [FeinnLocale] instance with another object for equality.
     *
     * This method checks if two [FeinnLocale] instances are equal by comparing their underlying [NSLocale] objects.
     *
     * @param other Any? - The object to compare with.
     * @return Boolean - `true` if the objects have the same [locale], otherwise `false`.
     */
    override fun equals(other: Any?): Boolean {
        return locale == (other as? FeinnLocale)?.locale
    }
}

/**
 * Retrieves the default locale of the system and returns it as a [FeinnLocale] instance.
 *
 * This method creates a new [FeinnLocale] object and sets its `locale` property to the system's current locale
 * using the [NSLocale.systemLocale] property. It provides a convenient way to access the locale settings of
 * the device's operating system.
 *
 * @receiver FeinnLocale.Companion - The companion object of [FeinnLocale], used for calling this function.
 * @return FeinnLocale - A new instance of [FeinnLocale] initialized with the system's default locale.
 *
 * Example usage:
 * ```
 * val defaultLocale = FeinnLocale.getDefault()
 * println(defaultLocale) // Output: System's default locale (e.g., "en_US")
 * ```
 *
 * Note:
 * - The `locale` property is set using the [NSLocale.systemLocale] to access the system's default locale.
 */
public actual fun FeinnLocale.Companion.getDefault(): FeinnLocale {
    return FeinnLocale(NSLocale.currentLocale)
}

/**
 * Retrieves a [FeinnLocale] instance based on the specified region identifier.
 *
 * This function creates a new [NSLocale] using the given region identifier (e.g., "US" for United States)
 * and returns a corresponding [FeinnLocale] instance. The region identifier is typically a two-letter code
 * representing a geographic region, such as a country or territory.
 *
 * @param regionId String - The region identifier (e.g., "US", "FR", etc.) representing the desired locale.
 * @return [FeinnLocale] - A [FeinnLocale] instance initialized with the [NSLocale] for the specified region.
 *
 * Example usage:
 * ```
 * val locale = FeinnLocale.getFromRegionId("US")
 * println(locale) // Output: Locale for the United States (e.g., "en_US")
 * ```
 */
public actual fun FeinnLocale.Companion.getFromRegionId(regionId: String): FeinnLocale {
    val locale = NSLocale(localeIdentifier = regionId)
    return FeinnLocale(locale)
}