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

// reference: https://developer.apple.com/documentation/usernotifications/unnotificationcontent
internal class FeinnNotificationCenter(
    private val dispatcher: CoroutineDispatcher
) : FeinnMutableNotificationState() {

    private val coroutineScope = CoroutineScope(dispatcher)

    private val userNotification: UNUserNotificationCenter = UNUserNotificationCenter
        .currentNotificationCenter()

    init {
        userNotification.delegate = FeinnNotificationCenterDelegate()
    }

    override fun send() {
        coroutineScope.launch {
            sendWithCompletion()
        }
    }

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

    private fun builderContent(): FeinnNotificationContent {
        val content = feinnNotificationContent {
            notificationData = data
        }

        return content
    }

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

    private fun builderTriggered(): FeinnNotificationTriggered {
        val triggered = feinnNotificationTriggered {
            notificationTrigger = this@FeinnNotificationCenter.trigger
        }

        return triggered
    }

    inner class FeinnNotificationCenterDelegate : UNUserNotificationCenterDelegateProtocol,
        NSObject() {
        override fun userNotificationCenter(
            center: UNUserNotificationCenter,
            openSettingsForNotification: UNNotification?
        ) {
            // Asks the delegate to display the in-app notification settings.
            println("[INFO] Open settings for notification")
        }

        override fun userNotificationCenter(
            center: UNUserNotificationCenter,
            didReceiveNotificationResponse: UNNotificationResponse,
            withCompletionHandler: () -> Unit
        ) {
            // Handling the actions in your actionable notifications
            println("[INFO] Receive notification")
        }

        override fun userNotificationCenter(
            center: UNUserNotificationCenter,
            willPresentNotification: UNNotification,
            withCompletionHandler: (UNNotificationPresentationOptions) -> Unit
        ) {
            // Processing notifications in the foreground
            println("[INFO] Will present notification")
            withCompletionHandler(UNNotificationPresentationOptionAlert)
        }

    }


}