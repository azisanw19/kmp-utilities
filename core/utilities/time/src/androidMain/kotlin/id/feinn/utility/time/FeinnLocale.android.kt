package id.feinn.utility.time

import java.util.Locale

/**
 * A wrapper class for [Locale] that provides additional functionality and customization.
 *
 * The `FeinnLocale` class encapsulates a [Locale] instance and is intended for use in contexts
 * where locale-specific operations, such as date formatting, are needed.
 *
 * @property locale Locale - The encapsulated [Locale] instance. This property must be initialized
 *                           before use due to its `lateinit` modifier.
 *
 * Example usage:
 * ```
 * val feinnLocale = FeinnLocale().apply {
 *     locale = Locale.ENGLISH
 * }
 * println(feinnLocale.locale) // Output: "en"
 * ```
 *
 * Note:
 * - The [locale] property must be explicitly initialized before any method call; otherwise,
 *   an [UninitializedPropertyAccessException] will be thrown.
 */
public actual class FeinnLocale {

    public actual companion object {}

    public lateinit var locale: Locale

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
    val feinnLocale = FeinnLocale()
    feinnLocale.locale = Locale.getDefault()
    return feinnLocale
}