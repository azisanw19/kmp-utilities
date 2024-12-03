package id.feinn.utility.context

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

public actual val FeinnLocalPlatformContext: ProvidableCompositionLocal<FeinnLocalContext>
    get() = staticCompositionLocalOf {
        FeinnLocalContextImpl.instance
    }