package id.feinn.utility.context

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * A platform-specific implementation of a composition local for providing a context instance.
 *
 * `FeinnLocalPlatformContext` is a `ProvidableCompositionLocal` that supplies an instance of
 * `FeinnLocalContext`. This is implemented using `staticCompositionLocalOf`, ensuring a consistent
 * and static context instance is provided within the composition. By default, it provides the
 * singleton `FeinnLocalContextImpl.instance`.
 *
 * This property allows composables to access the platform-specific context (`FeinnLocalContext`)
 * seamlessly in a shared codebase.
 *
 * @return A `ProvidableCompositionLocal` that provides a `FeinnLocalContext` instance.
 */
public actual val FeinnLocalPlatformContext: ProvidableCompositionLocal<FeinnLocalContext>
    get() = staticCompositionLocalOf {
        FeinnLocalContextImpl.instance
    }
