package id.feinn.utility.notification

/**
 * Represents the core notification content data for Feinn notifications.
 *
 * @property title The main title/heading of the notification. Displayed prominently.
 * @property body The content text of the notification. Provides details or context.
 */
public data class FeinnNotificationData(
    val title: String,
    val body: String,
)