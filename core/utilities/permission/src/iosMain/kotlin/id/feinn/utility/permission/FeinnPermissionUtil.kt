package id.feinn.utility.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Composable function to observe and manage permission state changes based on lifecycle events.
 *
 * This function listens to lifecycle events (default is `ON_RESUME`) and checks the permission state.
 * If the specified permission is not granted, it refreshes the permission status. This is useful
 * when the user might have changed permissions in the Settings screen and returned to the app.
 *
 * @param permissionState The mutable state representing the current permission status. This object
 *                        will be updated when the permission status changes.
 * @param lifecycleEvent The lifecycle event that triggers the permission check. Defaults to `ON_RESUME`,
 *                       meaning the check will occur when the lifecycle enters the resumed state.
 *
 * Usage:
 * Place this Composable in your UI hierarchy where you want to manage permission state updates
 * reactively based on lifecycle changes.
 *
 * Example:
 * ```
 * PermissionLifecycleCheckerEffect(permissionState = yourPermissionState)
 * ```
 */
@Composable
internal fun PermissionLifecycleCheckerEffect(
    permissionState: FeinnMutablePermissionState,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_RESUME
) {
    // Check if the permission was granted when the lifecycle is resumed.
    // The user might've gone to the Settings screen and granted the permission.
    val permissionCheckerObserver = remember(permissionState) {
        LifecycleEventObserver { _, event ->
            // If the permission is revoked, check again.
            // We don't check if the permission was denied as that triggers a process restart.
            if (event == lifecycleEvent && permissionState.status != FeinnPermissionStatus.Granted) {
                permissionState.refreshPermissionStatus()
            }
        }
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, permissionCheckerObserver) {
        lifecycle.addObserver(permissionCheckerObserver)
        onDispose { lifecycle.removeObserver(permissionCheckerObserver) }
    }
}