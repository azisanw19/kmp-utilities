package id.feinn.utility.notification.feature.trigger

import id.feinn.utility.notification.FeinnNotificationTrigger
import platform.UserNotifications.UNNotificationTrigger

internal class FeinnNotificationTriggered {

    var notificationTrigger: FeinnNotificationTrigger? = null
    private var notificationTriggered: UNNotificationTrigger? = null

    fun builder(): FeinnNotificationTriggered {
        return this
    }

    fun build(): UNNotificationTrigger? {
        return notificationTriggered
    }

}

internal fun feinnNotificationTriggered(block: FeinnNotificationTriggered.() -> Unit): FeinnNotificationTriggered {
    val triggered = FeinnNotificationTriggered()
    triggered.block()
    return triggered.builder()
}