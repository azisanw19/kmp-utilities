package id.feinn.utility.notification.feature.trigger

import id.feinn.utility.notification.FeinnNotificationTrigger
import platform.UserNotifications.UNNotificationTrigger

/**
 * The `FeinnNotificationTriggered` class represents a notification trigger
 * that is compatible with iOS in a Kotlin Multiplatform (KMP) project.
 *
 * This class holds information about `FeinnNotificationTrigger`, which defines
 * the conditions for triggering a notification, and `UNNotificationTrigger`,
 * which is the notification trigger from Apple's `UserNotifications` framework.
 */
internal class FeinnNotificationTriggered {

    /**
     * Stores information about the custom notification trigger `FeinnNotificationTrigger`.
     */
    var notificationTrigger: FeinnNotificationTrigger? = null

    /**
     * Stores the `UNNotificationTrigger` used by the iOS UserNotifications framework.
     */
    private var notificationTriggered: UNNotificationTrigger? = null

    /**
     * Builder method to return the current instance of `FeinnNotificationTriggered`.
     *
     * @return The instance of `FeinnNotificationTriggered`.
     */
    fun builder(): FeinnNotificationTriggered {
        return this
    }

    /**
     * Builds and returns the configured notification trigger.
     *
     * @return The `UNNotificationTrigger` object if set, or `null` if not configured.
     */
    fun build(): UNNotificationTrigger? {
        return notificationTriggered
    }
}

/**
 * Extension function to build a `FeinnNotificationTriggered` instance using a DSL.
 *
 * This function allows configuring the notification trigger using a lambda block,
 * providing flexibility in defining notification triggers in a KMP environment.
 *
 * @param block A configuration block applied to `FeinnNotificationTriggered`.
 * @return A configured instance of `FeinnNotificationTriggered`.
 */
internal fun feinnNotificationTriggered(block: FeinnNotificationTriggered.() -> Unit): FeinnNotificationTriggered {
    val triggered = FeinnNotificationTriggered()
    triggered.block()
    return triggered.builder()
}
