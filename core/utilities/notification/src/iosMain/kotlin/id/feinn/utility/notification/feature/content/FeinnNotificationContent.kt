package id.feinn.utility.notification.feature.content

import id.feinn.utility.notification.FeinnNotificationData
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationContent

internal class FeinnNotificationContent {

    var notificationData: FeinnNotificationData? = null
    private var notificationContent: UNNotificationContent? = null

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

    @Throws(IllegalArgumentException::class)
    fun build(): UNNotificationContent {
        checkNotNull(notificationContent) { "notification content cannot be null" }

        return notificationContent!!
    }

    @Throws(IllegalStateException::class)
    private fun mutableNotificationContent(): UNMutableNotificationContent {
        checkNotNull(notificationData) { "Notification data cannot be null" }

        val content = UNMutableNotificationContent()
        content.setTitle(notificationData!!.title)
        content.setBody(notificationData!!.body)

        return content
    }

}

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