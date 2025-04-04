package id.feinn.utility.notification.feature

import id.feinn.utility.notification.FeinnMutableNotificationState
import id.feinn.utility.notification.feature.content.FeinnNotificationContent
import id.feinn.utility.notification.feature.content.feinnNotificationContent
import id.feinn.utility.notification.feature.request.FeinnNotificationRequest
import id.feinn.utility.notification.feature.request.feinnNotificationRequest
import id.feinn.utility.notification.feature.trigger.FeinnNotificationTriggered
import id.feinn.utility.notification.feature.trigger.feinnNotificationTriggered
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

/**
 * iOS implementation of [FeinnMutableNotificationState] using Apple's UserNotifications framework.
 * Handles the creation, scheduling, and presentation of notifications on iOS platforms.
 *
 * This class:
 * - Wraps UNUserNotificationCenter for notification delivery
 * - Manages notification content, triggers, and requests
 * - Handles notification delegation and user interactions
 * - Provides coroutine-based async operations
 *
 * @property dispatcher The [CoroutineDispatcher] used for background notification operations
 *
 * @see <a href="https://developer.apple.com/documentation/usernotifications/unnotificationcontent">UNNotificationContent</a>
 */
internal class FeinnNotificationCenter(
    private val dispatcher: CoroutineDispatcher
) : FeinnMutableNotificationState() {

    private val coroutineScope = CoroutineScope(dispatcher)

    /**
     * The central notification management object for iOS.
     * Handles all notification-related activities for the app.
     */
    private val userNotification: UNUserNotificationCenter = UNUserNotificationCenter
        .currentNotificationCenter()

    init {
        userNotification.delegate = FeinnNotificationCenterDelegate()
    }

    /**
     * Sends the notification asynchronously using the configured dispatcher.
     * On iOS, this creates and schedules a UNNotificationRequest.
     */
    override fun send() {
        coroutineScope.launch {
            sendWithCompletion()
        }
    }

    /**
     * Internal method that handles the actual notification delivery with completion callback.
     *
     * @return Error description if the request fails, null if successful
     */
    private suspend fun sendWithCompletion() : String? = suspendCancellableCoroutine { continuation ->
        val content: FeinnNotificationContent = builderContent()
        val triggered: FeinnNotificationTriggered = builderTriggered()
        val request: FeinnNotificationRequest = builderRequest(content, triggered)

        userNotification.addNotificationRequest(
            request = request.build(),
            withCompletionHandler = { error ->
                if (error != null) {
                    continuation.resumeWith(Result.success(error.localizedDescription))
                    println("[ERROR] ${error.localizedDescription}")
                } else {
                    println("[INFO] Success")
                    continuation.resumeWith(Result.success(null))
                }
            }
        )
    }

    /**
     * Builds the notification content using current configuration.
     */
    private fun builderContent(): FeinnNotificationContent {
        val content = feinnNotificationContent {
            notificationData = data
        }
        return content
    }

    /**
     * Builds the notification request with content and trigger.
     *
     * @throws IllegalStateException if identifier is not set
     */
    @Throws(IllegalStateException::class)
    private fun builderRequest(
        content: FeinnNotificationContent,
        triggered: FeinnNotificationTriggered
    ): FeinnNotificationRequest {
        checkNotNull(identifier) { "identifier cannot be null" }

        val request = feinnNotificationRequest {
            this.identifier = this@FeinnNotificationCenter.identifier
            this.content = content.build()
            this.trigger = triggered.build()
        }

        return request
    }

    /**
     * Builds the notification trigger based on current configuration.
     */
    private fun builderTriggered(): FeinnNotificationTriggered {
        val triggered = feinnNotificationTriggered {
            notificationTrigger = this@FeinnNotificationCenter.trigger
        }
        return triggered
    }

    /**
     * Delegate class that handles iOS notification interactions and presentation.
     * Implements all required UNUserNotificationCenterDelegate methods.
     */
    inner class FeinnNotificationCenterDelegate : UNUserNotificationCenterDelegateProtocol,
        NSObject() {

        /**
         * Called when the app should display notification settings.
         */
        override fun userNotificationCenter(
            center: UNUserNotificationCenter,
            openSettingsForNotification: UNNotification?
        ) {
            println("[INFO] Open settings for notification")
        }

        /**
         * Called when the user interacts with a notification.
         */
        override fun userNotificationCenter(
            center: UNUserNotificationCenter,
            didReceiveNotificationResponse: UNNotificationResponse,
            withCompletionHandler: () -> Unit
        ) {
            println("[INFO] Receive notification")
            withCompletionHandler()
        }

        /**
         * Called when a notification should be presented in the foreground.
         */
        override fun userNotificationCenter(
            center: UNUserNotificationCenter,
            willPresentNotification: UNNotification,
            withCompletionHandler: (UNNotificationPresentationOptions) -> Unit
        ) {
            println("[INFO] Will present notification")
            withCompletionHandler(UNNotificationPresentationOptionAlert)
        }
    }
}