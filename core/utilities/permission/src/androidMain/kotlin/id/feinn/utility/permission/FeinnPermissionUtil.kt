package id.feinn.utility.permission

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Checks whether the specified permission is granted.
 *
 * This function wraps the `ContextCompat.checkSelfPermission` method to check if a specific
 * permission has been granted by the user for the current context.
 *
 * @param permission The permission to be checked. This is a string that specifies the permission,
 *                   e.g., `Manifest.permission.CAMERA` or `Manifest.permission.ACCESS_FINE_LOCATION`.
 * @return `true` if the permission is granted, `false` if the permission is denied.
 */
internal fun Context.checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

/**
 * Checks whether the rationale for requesting a specific permission should be shown to the user.
 *
 * This function uses `ActivityCompat.shouldShowRequestPermissionRationale` to determine whether
 * the app should display an explanation to the user about why the permission is needed, typically
 * when the user has previously denied the permission request.
 *
 * @param permission The permission to be checked. This is a string that specifies the permission,
 *                   e.g., `Manifest.permission.CAMERA` or `Manifest.permission.ACCESS_FINE_LOCATION`.
 * @return `true` if the rationale should be shown, `false` otherwise.
 */
internal fun Activity.shouldShowRationale(permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
}

/**
 * Composable function that checks permission status and reacts to lifecycle events.
 *
 * This effect listens to the specified lifecycle event (e.g., `ON_RESUME`) and refreshes the
 * permission status if the permission is not granted. This is particularly useful when the user
 * might have granted or revoked the permission from the Settings screen.
 *
 * @param permissionState The current permission state that will be checked and updated.
 * @param lifecycleEvent The lifecycle event to trigger the permission check. Defaults to `ON_RESUME`.
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

/**
 * Finds the `Activity` associated with the current context.
 *
 * This function traverses the context hierarchy to find the `Activity` object. It assumes that the
 * context is either an `Activity` or wrapped in a `ContextWrapper` that contains an `Activity`.
 *
 * @return The `Activity` associated with the current context.
 * @throws IllegalStateException if the context is not an `Activity` or wrapped inside one.
 */
internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}
