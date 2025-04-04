package id.feinn.utility.notification.feature.manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import id.feinn.utility.notification.FeinnAndroidChannel
import kotlin.jvm.Throws

/**
 * Android notification manager wrapper that handles channel creation and notification system access.
 *
 * This class:
 * - Manages Android notification channels (API 26+)
 * - Provides safe access to system NotificationManager
 * - Validates required configuration before building
 *
 * @property context The Android context used to access notification services
 */
internal class FeinnNotificationManager(
    private val context: Context
) {
    /**
     * The Android channel configuration for this notification.
     * Required for Android 8.0 (API 26) and above.
     */
    var feinnAndroidChannel: FeinnAndroidChannel? = null

    /**
     * Unique identifier for the notification channel/group.
     * Used for channel creation and notification management.
     */
    var identifier: String? = null

    private var notificationManager: NotificationManager? = null

    /**
     * Validates configuration and prepares the notification manager.
     *
     * @return This instance for method chaining
     * @throws IllegalStateException if required fields (channel or identifier) are null
     */
    @Throws(IllegalStateException::class)
    fun builder(): FeinnNotificationManager {
        checkNotNull(feinnAndroidChannel) { "Channel cannot be null" }
        checkNotNull(identifier) { "identifier cannot be null" }

        createNotificationChannel()
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        return this
    }

    /**
     * Returns the configured system NotificationManager.
     *
     * @return The initialized NotificationManager instance
     * @throws IllegalStateException if called before successful builder() execution
     */
    @Throws(IllegalStateException::class)
    fun build(): NotificationManager {
        checkNotNull(notificationManager) { "notificationManager cannot be null" }
        return notificationManager!!
    }

    /**
     * Creates the notification channel on Android 8.0+ devices.
     *
     * @throws IllegalStateException if required fields (channel or identifier) are null
     */
    @Throws(IllegalStateException::class)
    private fun createNotificationChannel() {
        checkNotNull(feinnAndroidChannel) { "Channel cannot be null" }
        checkNotNull(identifier) { "identifier cannot be null" }

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                feinnAndroidChannel?.id ?: identifier,
                feinnAndroidChannel!!.name,
                importance
            )

            feinnAndroidChannel!!.description?.let {
                channel.description = it
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}

/**
 * DSL-style builder function for creating and configuring a [FeinnNotificationManager].
 *
 * @param context The Android context used for notification services
 * @param block Configuration lambda to set up the manager properties
 * @return Configured and built [FeinnNotificationManager] instance
 */
internal fun feinnNotificationManager(
    context: Context,
    block: FeinnNotificationManager.() -> Unit
): FeinnNotificationManager {
    val manager = FeinnNotificationManager(context = context)
    manager.block()
    return manager.builder()
}