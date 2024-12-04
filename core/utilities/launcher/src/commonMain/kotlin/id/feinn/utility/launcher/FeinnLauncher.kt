package id.feinn.utility.launcher

import androidx.compose.runtime.Composable
import id.feinn.utility.context.FeinnLocalContext

/**
 * An expected class for platform-specific URI launching functionality.
 *
 * This class defines a platform-agnostic interface for launching URIs, which must be
 * implemented in specific platform modules (e.g., Android, iOS). The actual implementation
 * handles platform-specific behavior for opening URIs or displaying documents.
 */
public expect class FeinnLauncher {
    /**
     * Launches a URI using the platform-specific implementation.
     *
     * This method is expected to handle URIs in ways suitable for the underlying platform.
     * For example, on Android, it may use `Intent`, and on iOS, it may use `UIApplication`
     * or `UIDocumentInteractionController`.
     *
     * @param uri The URI string to be launched.
     * @throws Platform-specific exceptions may occur if the URI is invalid or cannot be handled.
     */
    public fun launch(uri: String)
}

/**
 * An expected composable function for creating or remembering an instance of `FeinnLauncher`.
 *
 * This function provides a way to use `FeinnLauncher` in a platform-agnostic way within Jetpack Compose.
 * The actual implementation will handle the creation or retrieval of the `FeinnLauncher` instance
 * for the specific platform.
 *
 * @param context The platform-specific context (e.g., `Context` on Android or `FeinnLocalContext` on iOS).
 * @return An instance of `FeinnLauncher` appropriate for the current platform.
 */
@Composable
public expect fun rememberFeinnLauncer(context: FeinnLocalContext): FeinnLauncher