package id.feinn.utility.notification

import androidx.compose.runtime.Composable
import id.feinn.utility.context.FeinnLocalContext

/**
 * Composable function that remembers and provides the Feinn notification state.
 *
 * @param isPermissionGranted Callback invoked with the current notification permission status.
 *                            Receives `true` if permissions are granted, `false` otherwise.
 * @return The current [FeinnNotificationState] instance for managing notifications.
 */
@Composable
public expect fun rememberFeinnNotification(
    isPermissionGranted: (Boolean) -> Unit = {}
) : FeinnNotificationState

/**
 * Represents the state and configuration of a Feinn notification.
 * Provides methods to build and send platform-agnostic notifications.
 */
public interface FeinnNotificationState {

    /**
     * Companion object providing factory methods for notification state creation.
     */
    public companion object {}

    /**
     * The notification content data containing title and body text.
     * Set to `null` to clear any existing notification content.
     */
    public var data: FeinnNotificationData?

    /**
     * Configuration for when and how the notification should be triggered.
     */
    public var trigger: FeinnNotificationTrigger

    /**
     * Unique identifier for the notification.
     * Used for updating/canceling existing notifications.
     * If null, a default identifier will be generated.
     */
    public var identifier: String?

    /**
     * Android-specific channel configuration (required for Android 8.0+).
     * Ignored on other platforms.
     */
    public var androidChannel: FeinnAndroidChannel?

    /**
     * Sends/updates the notification with the current configuration.
     * For scheduled notifications, this will set up the appropriate trigger.
     */
    public fun send()
}

/**
 * Builds a new [FeinnNotificationState] instance with the given configuration.
 *
 * @param context The local context for platform-specific notification handling.
 * @param block Configuration lambda to set up the notification properties.
 * @return A fully configured [FeinnNotificationState] instance ready for use.
 */
public expect fun FeinnNotificationState.Companion.build(context: FeinnLocalContext, block: FeinnNotificationState.() -> Unit) : FeinnNotificationState