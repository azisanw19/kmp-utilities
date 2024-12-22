package id.feinn.utility.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import id.feinn.utility.permission.FeinnPermissionType
import id.feinn.utility.permission.isGranted
import id.feinn.utility.permission.rememberFeinnPermissionState

// reference: https://developer.apple.com/documentation/usernotifications/unnotificationcontent
internal abstract class FeinnMutableNotificationState : FeinnNotificationState {

    override var data: FeinnNotificationData? = null
    override var trigger: FeinnNotificationTrigger = FeinnNotificationTrigger.Now
    override var identifier: String? = null

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
