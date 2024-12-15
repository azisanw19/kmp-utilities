package id.feinn.utility.permission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Remembers and provides a [FeinnPermissionState] implementation for the Android platform.
 *
 * This implementation:
 * - Initializes the permission state using the current [LocalContext].
 * - Monitors lifecycle events to refresh the permission status when the activity is resumed.
 * - Uses [rememberLauncherForActivityResult] to handle permission requests and updates the permission state.
 *
 * @param permission The permission type that is being requested. Should be of type [FeinnPermissionType].
 * @param onPermissionResult A callback that is invoked when the permission result is obtained.
 * The callback provides a [Boolean] indicating whether the permission was granted (true) or denied (false).
 * @return A [FeinnPermissionState] instance configured for the given [permission].
 */
@Composable
public actual fun rememberFeinnPermissionState(
    permission: FeinnPermissionType,
    onPermissionResult: (Boolean) -> Unit
): FeinnPermissionState {
    val context = LocalContext.current
    val permissionState = remember(permission) {
        FeinnMutablePermissionState(permission, context, context.findActivity())
    }

    // Refresh the permission status when the lifecycle is resumed
    PermissionLifecycleCheckerEffect(permissionState)

    // Remember RequestPermission launcher and assign it to permissionState
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        permissionState.refreshPermissionStatus()
        onPermissionResult(it)
    }
    DisposableEffect(permissionState, launcher) {
        permissionState.launcher = launcher
        onDispose {
            permissionState.launcher = null
        }
    }

    return permissionState
}