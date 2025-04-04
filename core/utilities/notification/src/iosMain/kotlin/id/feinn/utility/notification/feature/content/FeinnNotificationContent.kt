package id.feinn.utility.notification.feature.content

import id.feinn.utility.notification.FeinnNotificationData
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationContent

/**
 * Represents the content of a notification in an iOS Kotlin Multiplatform (KMP) project.
 *
 * This class is responsible for configuring `UNNotificationContent`, which defines
 * the title, body, and other attributes of a local or remote notification.
 */
internal class FeinnNotificationContent {

    /**
     * The data associated with the notification, containing the title and body.
     * This must be set before building a mutable notification content.
     */
    var notificationData: FeinnNotificationData? = null

    /**
     * Stores the built `UNNotificationContent` instance.
     */
    private var notificationContent: UNNotificationContent? = null

    /**
     * Builds the `UNNotificationContent` based on whether the notification is remote.
     *
     * If the notification is remote, a default `UNNotificationContent` instance is used.
     * Otherwise, a mutable notification content is created based on `notificationData`.
     *
     * @param isRemoteNotification Indicates if the notification is from a remote source.
     * @return The instance of `FeinnNotificationContent` after configuring the content.
     */
    fun builder(
        isRemoteNotification: Boolean = false
    ): FeinnNotificationContent {
        notificationContent = if (isRemoteNotification) {
            UNNotificationContent()
        } else {
            mutableNotificationContent()
        }

        return this
    }

    /**
     * Returns the configured `UNNotificationContent`.
     *
     * @throws IllegalArgumentException if `notificationContent` is `null`.
     * @return The `UNNotificationContent` instance.
     */
    @Throws(IllegalArgumentException::class)
    fun build(): UNNotificationContent {
        checkNotNull(notificationContent) { "Notification content cannot be null" }
        return notificationContent!!
    }

    /**
     * Creates a mutable `UNMutableNotificationContent` with title and body from `notificationData`.
     *
     * @throws IllegalStateException if `notificationData` is `null`.
     * @return A configured `UNMutableNotificationContent` instance.
     */
    @Throws(IllegalStateException::class)
    private fun mutableNotificationContent(): UNMutableNotificationContent {
        checkNotNull(notificationData) { "Notification data cannot be null" }

        val content = UNMutableNotificationContent()
        content.setTitle(notificationData!!.title)
        content.setBody(notificationData!!.body)

        return content
    }
}

/**
 * DSL function to create a `FeinnNotificationContent` instance.
 *
 * This function allows configuring notification content using a lambda block.
 * It determines whether the notification is remote and applies the appropriate builder logic.
 *
 * @param isFromRemote Indicates whether the notification originates from a remote source.
 * @param block A configuration block applied to `FeinnNotificationContent`.
 * @return A fully built instance of `FeinnNotificationContent`.
 */
internal fun feinnNotificationContent(
    isFromRemote: Boolean = false,
    block: FeinnNotificationContent.() -> Unit
): FeinnNotificationContent {
    val content = FeinnNotificationContent()
    content.block()
    return content.builder(
        isRemoteNotification = isFromRemote
    )
}
