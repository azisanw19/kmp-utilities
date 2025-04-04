package id.feinn.utility.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import id.feinn.utility.permission.FeinnPermissionType
import id.feinn.utility.permission.isGranted
import id.feinn.utility.permission.rememberFeinnPermissionState

/**
 * Abstract base class implementing mutable notification state management for multi-platform use.
 * Provides core functionality for [FeinnNotificationState] implementations on both Android and iOS.
 *
 * Handles:
 * - Notification content data (title/body)
 * - Trigger configuration
 * - Permission management
 * - Platform-specific channel configuration (Android only)
 *
 * iOS-specific behavior:
 * - Uses UNNotificationContent for iOS notifications (Apple's UserNotifications framework)
 * - Manages permission requests through native iOS APIs
 * - Ignores androidChannel property on iOS platform
 *
 * @see <a href="https://developer.apple.com/documentation/usernotifications/unnotificationcontent">Apple's UNNotificationContent</a>
 */
internal abstract class FeinnMutableNotificationState : FeinnNotificationState {

    /**
     * The notification content data containing title and body text.
     * Maps to UNNotificationContent's title and body properties on iOS.
     */
    override var data: FeinnNotificationData? = null

    /**
     * Specifies when/how the notification should be triggered.
     * On iOS, translates to UNNotificationTrigger equivalents.
     */
    override var trigger: FeinnNotificationTrigger = FeinnNotificationTrigger.Now

    /**
     * Unique identifier for the notification.
     * On iOS, used as the UNNotificationRequest identifier.
     */
    override var identifier: String? = null

    /**
     * Android-specific channel configuration.
     * Ignored on iOS platform as notifications use different grouping.
     */
    override var androidChannel: FeinnAndroidChannel? = null

    /**
     * Composable function that manages notification permission state.
     * On iOS, uses UNUserNotificationCenter for permission requests.
     *
     * @param isPermissionGranted Callback that receives permission state:
     *                           - `true` if authorized (UNAuthorizationStatus.authorized)
     *                           - `false` if denied/provisional/notDetermined
     *
     * iOS-specific behavior:
     * - First call checks current authorization status
     * - Automatically requests permission if status is .notDetermined
     * - Respects provisional authorization (.provisional)
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