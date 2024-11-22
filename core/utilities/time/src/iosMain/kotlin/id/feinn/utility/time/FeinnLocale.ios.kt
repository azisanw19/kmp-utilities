package id.feinn.utility.time

import platform.Foundation.NSLocale
import platform.Foundation.systemLocale

public actual class FeinnLocale {

    public actual companion object {}

    public lateinit var locale: NSLocale

}

public actual fun FeinnLocale.Companion.getDefault(): FeinnLocale {
    val feinnLocale = FeinnLocale()
    feinnLocale.locale = NSLocale.systemLocale
    return feinnLocale
}