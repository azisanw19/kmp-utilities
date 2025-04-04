package id.feinn.utility.notification.feature.request

import platform.UserNotifications.UNNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationTrigger

/**
 * Represents a notification request in an iOS-compatible Kotlin Multiplatform (KMP) project.
 *
 * This class encapsulates the necessary components to create an `UNNotificationRequest`,
 * which is used to schedule local notifications on iOS.
 */
internal class FeinnNotificationRequest {

    /**
     * A unique identifier for the notification request.
     * This identifier is required and must not be `null`.
     */
    var identifier: String? = null

    /**
     * The content of the notification, represented by `UNNotificationContent`.
     * This includes the title, body, sound, and other notification details.
     */
    var content: UNNotificationContent? = null

    /**
     * The trigger that determines when the notification should be delivered.
     * This can be time-based, location-based, or other types of `UNNotificationTrigger`.
     */
    var trigger: UNNotificationTrigger? = null

    /**
     * The constructed `UNNotificationRequest` object, created using the provided parameters.
     * This property is `null` until `builder()` is called.
     */
    private var notificationRequest: UNNotificationRequest? = null

    /**
     * Builds the notification request by validating the required parameters
     * (`identifier` and `content`) and then creates a `UNNotificationRequest`.
     *
     * @throws IllegalStateException if `identifier` or `content` is `null`.
     * @return The current instance of `FeinnNotificationRequest` after constructing the request.
     */
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

    /**
     * Returns the built `UNNotificationRequest` object.
     *
     * @throws IllegalStateException if the request has not been built using `builder()`.
     * @return The configured `UNNotificationRequest` object.
     */
    @Throws(IllegalStateException::class)
    fun build(): UNNotificationRequest {
        checkNotNull(notificationRequest) { "notificationRequest cannot be null" }
        return notificationRequest!!
    }
}

/**
 * DSL-style function to create and configure a `FeinnNotificationRequest` instance.
 *
 * This function allows defining a notification request using a lambda block,
 * ensuring all required parameters are set before returning the built request.
 *
 * @param block A lambda block used to configure the notification request.
 * @throws IllegalStateException if `identifier` or `content` is not set.
 * @return A configured instance of `FeinnNotificationRequest`.
 */
@Throws(IllegalStateException::class)
internal fun feinnNotificationRequest(block: FeinnNotificationRequest.() -> Unit): FeinnNotificationRequest {
    val request = FeinnNotificationRequest()
    request.block()
    return request.builder()
}
