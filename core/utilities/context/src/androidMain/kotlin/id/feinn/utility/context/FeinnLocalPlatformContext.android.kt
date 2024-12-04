package id.feinn.utility.context

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.ui.platform.LocalContext

/**
 * A platform-specific implementation of a composition local for providing context.
 *
 * On Android, `FeinnLocalPlatformContext` is implemented as a `ProvidableCompositionLocal`
 * that retrieves the current `Context` using `LocalContext`. This allows composables
 * to access the Android `Context` within a Jetpack Compose environment.
 *
 * @return A `ProvidableCompositionLocal` that provides the current `FeinnLocalContext`
 *         (an alias for `Context` on Android).
 */
public actual val FeinnLocalPlatformContext: ProvidableCompositionLocal<FeinnLocalContext>
    get() = LocalContext