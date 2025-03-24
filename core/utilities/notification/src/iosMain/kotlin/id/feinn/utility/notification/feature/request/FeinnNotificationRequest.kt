package id.feinn.utility.notification.feature.request

import platform.UserNotifications.UNNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationTrigger

internal class FeinnNotificationRequest {

    var identifier: String? = null
    var content: UNNotificationContent? = null
    var trigger: UNNotificationTrigger? = null
    private var notificationRequest: UNNotificationRequest? = null

    @Throws(IllegalStateException::class)
    fun builder(): FeinnNotificationRequest {
        checkNotNull(identifier) { "Identifier cannot be null" }
        checkNotNull(content) { "Content cannot be null" }

        notificationRequest = UNNotificationRequest.requestWithIdentifier(
            identifier = identifier!!,
            content = content!!,
            trigger = trigger
        )
        return this
    }

    @Throws(IllegalStateException::class)
    fun build(): UNNotificationRequest {
        checkNotNull(notificationRequest) { "notificationRequest cannot be null" }

        return notificationRequest!!
    }
}

@Throws(IllegalStateException::class)
internal fun feinnNotificationRequest(block: FeinnNotificationRequest.() -> Unit): FeinnNotificationRequest {
    val request = FeinnNotificationRequest()
    request.block()
    return request.builder()
}