package id.feinn.utility.time

import platform.Foundation.NSLocale
import platform.Foundation.systemLocale

/**
 * The actual implementation of [FeinnLocale] for iOS, representing a locale using [NSLocale].
 *
 * This class encapsulates the platform-specific [NSLocale] object from the iOS SDK. It provides functionality
 * to interact with system locale information, such as language, country, and variant. The `locale` property
 * stores the actual iOS locale instance.
 *
 * Example usage:
 * ```
 * val currentLocale = FeinnLocale.getDefault()
 * println(currentLocale) // Output: Current system locale
 * ```
 */
public actual class FeinnLocale {

    /**
     * The companion object for [FeinnLocale], which provides static methods related to the class.
     */
    public actual companion object {}

    /**
     * The actual [NSLocale] object that holds the locale information.
     */
    public lateinit var locale: NSLocale
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
    val feinnLocale = FeinnLocale()
    feinnLocale.locale = NSLocale.systemLocale
    return feinnLocale
}
