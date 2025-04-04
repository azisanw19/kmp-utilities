package id.feinn.utility.notification.feature.content

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import id.feinn.utility.notification.FeinnNotificationAndroid
import id.feinn.utility.notification.FeinnNotificationData

/**
 * Handles construction of Android notification content using [NotificationCompat].
 *
 * This class:
 * - Builds notification content with title, body, and icon
 * - Manages notification channel association
 * - Provides type-safe notification content creation
 *
 * @property context The Android [Context] for resource access
 * @property channelId The notification channel ID (required for Android 8.0+)
 */
internal class FeinnNotificationContent(
    private val context: Context,
    private val channelId: String
) {
    /**
     * The notification content data containing title and body text.
     * Set to null to skip content configuration.
     */
    var notificationData: FeinnNotificationData? = null

    private var notificationContent: Notification? = null

    /**
     * Constructs the notification content with current configuration.
     *
     * @return This instance for method chaining
     *
     * Configures:
     * - Title (if provided in [notificationData])
     * - Body text (if provided in [notificationData])
     * - Small icon (from [FeinnNotificationAndroid] singleton)
     */
    fun builder(): FeinnNotificationContent {
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
        val feinnNotificationAndroid = FeinnNotificationAndroid.getInstance()

        notificationData?.title?.let { title ->
            notificationBuilder.setContentTitle(title)
        }

        notificationData?.body?.let { body ->
            notificationBuilder.setContentText(body)
        }

        notificationBuilder.setSmallIcon(feinnNotificationAndroid.getDrawableId())

        notificationContent = notificationBuilder.build()

        return this
    }

    /**
     * Returns the built [Notification] object.
     *
     * @return The fully configured Notification instance
     * @throws IllegalArgumentException if called before successful [builder()] execution
     */
    @Throws(IllegalArgumentException::class)
    fun build(): Notification {
        checkNotNull(notificationContent) {
            "Notification content cannot be null - call builder() first"
        }
        return notificationContent!!
    }
}

/**
 * DSL-style builder function for creating notification content.
 *
 * @param context The Android [Context] for resource access
 * @param channelId The notification channel ID (required for Android 8.0+)
 * @param block Configuration lambda to set notification properties
 * @return Configured [FeinnNotificationContent] instance ready for building
 */
internal fun feinnNotificationContent(
    context: Context,
    channelId: String,
    block: FeinnNotificationContent.() -> Unit
): FeinnNotificationContent {
    val content = FeinnNotificationContent(
        context = context,
        channelId = channelId
    )
    content.block()
    return content.builder()
}