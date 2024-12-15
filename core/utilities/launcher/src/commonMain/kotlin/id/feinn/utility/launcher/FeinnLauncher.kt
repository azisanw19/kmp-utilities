package id.feinn.utility.launcher

import androidx.compose.runtime.Composable

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
 * A composable function to create or remember an instance of `FeinnLauncher`.
 *
 * This function provides a platform-agnostic way to use `FeinnLauncher` within Jetpack Compose.
 * It ensures that an appropriate instance of `FeinnLauncher` is either created or retrieved
 * based on the platform-specific implementation.
 *
 * @return An instance of `FeinnLauncher` suitable for the current platform.
 */
@Composable
public expect fun rememberFeinnLauncer(): FeinnLauncher