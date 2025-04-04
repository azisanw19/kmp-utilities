package id.feinn.utility.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import id.feinn.utility.permission.FeinnPermissionType
import id.feinn.utility.permission.isGranted
import id.feinn.utility.permission.rememberFeinnPermissionState

/**
 * Abstract base class implementing mutable notification state management.
 * Provides core functionality for [FeinnNotificationState] implementations.
 *
 * This class handles:
 * - Notification data storage (title, body, etc.)
 * - Trigger configuration
 * - Permission management
 * - Android-specific channel configuration
 */
internal abstract class FeinnMutableNotificationState : FeinnNotificationState {

    /** Current notification content data (title and body) */
    override var data: FeinnNotificationData? = null

    /** Specifies when/how the notification should be triggered */
    override var trigger: FeinnNotificationTrigger = FeinnNotificationTrigger.Now

    /** Unique identifier for the notification (null generates default) */
    override var identifier: String? = null

    /** Android-specific channel configuration (required for API 26+) */
    override var androidChannel: FeinnAndroidChannel? = null

    /**
     * Composable function that manages notification permission state.
     * Automatically requests permission if not granted.
     *
     * @param isPermissionGranted Callback that receives the current permission state:
     *                           - `true` if notifications are permitted
     *                           - `false` if denied or not yet requested
     */
    @Composable
    internal fun checkPermissionState(
        isPermissionGranted: (Boolean) -> Unit
    ) {
        val notificationPermission = rememberFeinnPermissionState(
            permission = FeinnPermissionType.Notification,
            onPermissionResult = {
                isPermissionGranted(it)
            }
        )

        LaunchedEffect(notificationPermission) {
            if (!notificationPermission.status.isGranted) {
                notificationPermission.launchPermissionRequest()
            } else {
                isPermissionGranted(true)
            }
        }
    }

}