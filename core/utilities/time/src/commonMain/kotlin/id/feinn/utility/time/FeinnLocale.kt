package id.feinn.utility.time

/**
 * This class is designed to be implemented platform-specifically, allowing different platforms to provide
 * their own locale handling. It encapsulates the system's locale and provides functionality to work
 * with locale-specific data, such as date-time formatting.
 *
 * Since this is an expected class, its actual implementation will vary depending on the platform, ensuring
 * compatibility with the platform's locale system.
 *
 * Example usage:
 * ```
 * val feinnLocale = FeinnLocale()
 * // Platform-specific functionality will be implemented in actual code
 * ```
 *
 * The companion object provides static methods related to locale functionality for the [FeinnLocale] class.
 */
public expect class FeinnLocale {

    /**
     * Companion object to hold any static properties or methods for [FeinnLocale].
     */
    public companion object

}

/**
 * Retrieves the default locale of the system and returns it as a [FeinnLocale] instance.
 *
 * This function is expected to be implemented in platform-specific code to return the default locale of the system.
 * The exact behavior of how the default locale is obtained will depend on the target platform.
 *
 * @receiver FeinnLocale.Companion - The companion object of [FeinnLocale].
 * @return FeinnLocale - A new instance of [FeinnLocale] initialized with the default system locale.
 *
 * Example usage:
 * ```
 * val defaultLocale = FeinnLocale.getDefault()
 * println(defaultLocale) // Output: System's default locale, e.g., "en_US"
 * ```
 *
 * Note:
 * - The actual implementation of retrieving the default locale will depend on the platform.
 */
public expect fun FeinnLocale.Companion.getDefault(): FeinnLocale
