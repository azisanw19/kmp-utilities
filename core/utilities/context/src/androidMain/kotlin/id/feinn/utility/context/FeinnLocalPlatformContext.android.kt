package id.feinn.utility.context

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.ui.platform.LocalContext

public actual val FeinnLocalPlatformContext: ProvidableCompositionLocal<FeinnLocalContext>
    get() = LocalContext