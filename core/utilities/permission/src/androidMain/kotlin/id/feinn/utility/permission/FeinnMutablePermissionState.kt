package id.feinn.utility.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Represents a mutable implementation of [FeinnPermissionState] for managing permissions on Android.
 *
 * This class handles permission status updates, requesting permissions, and launching settings
 * screens for managing app permissions. It is designed to be lifecycle-aware and works with
 * Android's [ActivityResultLauncher].
 *
 * @property permission The specific permission being managed, encapsulated by [FeinnPermissionType].
 * @property context The current [Context] used to check and request permissions.
 * @property activity The current [Activity] used to launch permission dialogs and settings screens.
 */
@Stable
internal class FeinnMutablePermissionState(
    override val permission: FeinnPermissionType,
    private val context: Context,
    private val activity: Activity
) : FeinnPermissionState {

    /**
     * The current status of the permission, dynamically updated as the system state changes.
     * Possible values include [FeinnPermissionStatus.Granted] and [FeinnPermissionStatus.Denied].
     */
    override var status: FeinnPermissionStatus by mutableStateOf(getPermissionStatus())

    /**
     * The [ActivityResultLauncher] used to initiate permission requests.
     *
     * This launcher must be initialized by a lifecycle-aware component before calling
     * [launchPermissionRequest]. It is cleared when no longer needed to prevent memory leaks.
     */
    internal var launcher: ActivityResultLauncher<String>? = null

    /**
     * Launches a permission request using the associated [launcher].
     *
     * If the permission does not require explicit user action (e.g., granted by default),
     * the method exits early. Throws an [IllegalStateException] if the [launcher] is null
     * when invoked.
     *
     * @throws IllegalStateException if the [launcher] is null when this method is called.
     */
    override fun launchPermissionRequest() {
        if (isPermissionNull()) return
        launcher?.launch(permission.permission!!)
            ?: throw IllegalStateException("ActivityResultLauncher cannot be null")
    }

    /**
     * Refreshes the current permission status by querying the system state.
     *
     * This method should be called whenever the permission state might have changed,
     * such as after returning from a permission request or settings screen.
     */
    internal fun refreshPermissionStatus() {
        status = getPermissionStatus()
    }

    /**
     * Launches the device's settings screen to allow the user to modify the app's permission state.
     *
     * This method navigates the user to the application's settings page, where permissions
     * can be manually adjusted.
     */
    override fun launchSettingRequest() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
        activity.startActivity(intent)
    }

    /**
     * Determines the current status of the permission by checking the system's permission state.
     *
     * @return The current [FeinnPermissionStatus]:
     * - [FeinnPermissionStatus.Granted] if the permission is granted.
     * - [FeinnPermissionStatus.Denied] if the permission is denied, optionally with a rationale flag.
     */
    private fun getPermissionStatus(): FeinnPermissionStatus {
        if (isPermissionNull()) return FeinnPermissionStatus.Granted
        val hasPermission = context.checkPermission(permission.permission!!)
        return if (hasPermission) {
            FeinnPermissionStatus.Granted
        } else {
            FeinnPermissionStatus.Denied(activity.shouldShowRationale(permission.permission))
        }
    }

    /**
     * Checks if the permission is null, indicating it is granted by default and does not
     * require explicit user action.
     *
     * @return `true` if the permission is null, `false` otherwise.
     */
    private fun isPermissionNull(): Boolean {
        return if (permission.permission == null) {
            println("[INFO]: ${permission.name} Granted by default")
            true
        } else {
            false
        }
    }
}
