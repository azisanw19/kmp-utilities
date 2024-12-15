package id.feinn.utility.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

/**
 * Composable function to create and remember the state of a permission.
 *
 * This function manages the lifecycle of a permission state object, ensuring it is updated
 * reactively based on lifecycle changes. It also allows you to handle the result of a permission
 * request using a callback.
 *
 * @param permission The type of permission to manage. This is an instance of `FeinnPermissionType`,
 *                   such as `FeinnPermissionType.Camera`.
 * @param onPermissionResult Callback invoked when the user responds to a permission request.
 *                           Receives a `Boolean` indicating whether the permission was granted (`true`)
 *                           or denied (`false`).
 * @return A `FeinnPermissionState` object representing the current state of the requested permission.
 *
 * Behavior:
 * - Remembers the permission state for the given `permission` type.
 * - Monitors the permission's lifecycle using `PermissionLifecycleCheckerEffect` to refresh the
 *   permission status when the app's lifecycle resumes.
 * - Manages a `launcher` for the permission request, attaching the `onPermissionResult` callback
 *   and cleaning it up when the Composable leaves the composition.
 *
 * Example Usage:
 * ```
 * val cameraPermissionState = rememberFeinnPermissionState(
 *     permission = FeinnPermissionType.Camera
 * ) { isGranted ->
 *     if (isGranted) {
 *         // Handle granted permission
 *     } else {
 *         // Handle denied permission
 *     }
 * }
 * ```
 */
@Composable
public actual fun rememberFeinnPermissionState(
    permission: FeinnPermissionType,
    onPermissionResult: (Boolean) -> Unit
): FeinnPermissionState {
    val permissionState = remember(permission) {
        permission.getMutablePermission()
    }

    // Refresh the permission status when the lifecycle is resumed
    PermissionLifecycleCheckerEffect(permissionState)

    DisposableEffect(permissionState, permissionState.launcher) {
        permissionState.launcher = onPermissionResult
        onDispose {
            permissionState.launcher = null
        }
    }

    return permissionState
}