package id.feinn.utility.context

import androidx.compose.runtime.ProvidableCompositionLocal

/**
 * An expected property for providing a platform-specific composition local for context.
 *
 * `FeinnLocalPlatformContext` is an expected `ProvidableCompositionLocal` that serves as a mechanism
 * to access the current `FeinnLocalContext` in a Jetpack Compose environment. Each platform must
 * provide an actual implementation that defines how the context is supplied and managed.
 *
 * This abstraction allows shared Compose code to work seamlessly with context functionality
 * across different platforms.
 *
 * @return A `ProvidableCompositionLocal` for accessing the platform-specific `FeinnLocalContext`.
 */
public expect val FeinnLocalPlatformContext: ProvidableCompositionLocal<FeinnLocalContext>
