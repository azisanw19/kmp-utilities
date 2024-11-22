package id.feinn.utility.time

import java.util.Locale

public actual class FeinnLocale {

    public actual companion object {}

    public lateinit var locale: Locale

}

public actual fun FeinnLocale.Companion.getDefault(): FeinnLocale {
    val feinnLocale = FeinnLocale()
    feinnLocale.locale = Locale.getDefault()
    return feinnLocale
}