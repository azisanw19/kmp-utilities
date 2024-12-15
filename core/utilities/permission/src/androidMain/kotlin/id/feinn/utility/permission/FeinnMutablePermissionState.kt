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
 * Represents a mutable implementation of [FeinnPermissionState] for Android.
 *
 * @property permission The type of permission being managed.
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
     * The current status of the permission, updated dynamically.
     */
    override var status: FeinnPermissionStatus by mutableStateOf(getPermissionStatus())

    /**
     * Launches a permission request using the associated launcher.
     *
     * @throws IllegalStateException if the [launcher] is null when this method is called.
     */
    override fun launchPermissionRequest() {
        launcher?.launch(
            permission.permission
        ) ?: throw IllegalStateException("ActivityResultLauncher cannot be null")
    }

    /**
     * The [ActivityResultLauncher] used to request permissions.
     * This is assigned and cleared dynamically via lifecycle-aware components.
     */
    internal var launcher: ActivityResultLauncher<String>? = null

    /**
     * Refreshes the current permission status by querying the system.
     */
    internal fun refreshPermissionStatus() {
        status = getPermissionStatus()
    }

    /**
     * Determines the current status of the permission by checking the system state.
     *
     * @return The current [FeinnPermissionStatus], either [FeinnPermissionStatus.Granted] or [FeinnPermissionStatus.Denied].
     */
    private fun getPermissionStatus(): FeinnPermissionStatus {
        val hasPermission = context.checkPermission(permission.permission)
        return if (hasPermission) {
            FeinnPermissionStatus.Granted
        } else {
            FeinnPermissionStatus.Denied(activity.shouldShowRationale(permission.permission))
        }
    }

    /**
     * Launches the device's settings screen to allow the user to modify the permission state.
     */
    override fun launchSettingRequest() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )

        activity.startActivity(intent)
    }

}