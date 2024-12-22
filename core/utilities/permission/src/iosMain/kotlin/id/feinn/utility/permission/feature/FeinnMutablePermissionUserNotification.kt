package id.feinn.utility.permission.feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import id.feinn.utility.permission.FeinnMutablePermissionState
import id.feinn.utility.permission.FeinnPermissionStatus
import id.feinn.utility.permission.FeinnPermissionType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UserNotifications.UNAuthorizationOptions
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import platform.UserNotifications.UNAuthorizationStatusEphemeral
import platform.UserNotifications.UNAuthorizationStatusNotDetermined
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNUserNotificationCenter

/**
 * A class representing mutable user notification permissions in a system.
 *
 * This class handles permission checks, status retrieval, and permission request for user notifications.
 * It uses a coroutine scope to manage asynchronous operations and updates the permission status accordingly.
 *
 * @param permission The type of permission (e.g., notifications) being requested.
 * @param options The authorization options used to request the permission.
 * @param dispatcher The dispatcher used for running coroutines.
 */
internal class FeinnMutablePermissionUserNotification(
    override val permission: FeinnPermissionType,
    private val options: UNAuthorizationOptions,
    private val dispatcher: CoroutineDispatcher
) : FeinnMutablePermissionState() {

    // Coroutine scope for managing asynchronous operations related to permission status
    private val coroutineScope = CoroutineScope(dispatcher)

    // Mutable state for storing the current permission status
    override var status: FeinnPermissionStatus by mutableStateOf(FeinnPermissionStatus.Denied(false))

    // A launcher function to handle the result of the permission request
    override var launcher: ((Boolean) -> Unit)? = null

    /**
     * Refreshes the current permission status by making a request to get the latest status.
     * This function launches a coroutine to retrieve the status.
     */
    override fun refreshPermissionStatus() {
        coroutineScope.launch {
            status = getPermissionStatus()
        }
    }

    /**
     * Initiates a permission request to the user, prompting them to grant or deny the notification permission.
     * This function makes a call to `requestAuthorizationWithOptions`.
     *
     * @throws IllegalStateException If there is an error in the permission request.
     */
    override fun launchPermissionRequest() {
        UNUserNotificationCenter.currentNotificationCenter().requestAuthorizationWithOptions(
            options = options,
            completionHandler = { result, error ->
                // If there is an error, throw an exception
                error?.let {
                    throw IllegalStateException(it.localizedDescription)
                }

                // Invoke the launcher function with the result of the permission request
                launcher?.invoke(result)
            }
        )
    }

    /**
     * Retrieves the current notification permission status.
     *
     * This function launches a coroutine to fetch the current settings from the Notification Center and
     * maps the result into a `FeinnPermissionStatus`.
     *
     * @return The permission status as a `FeinnPermissionStatus`.
     * @throws IllegalStateException If notification settings cannot be fetched.
     */
    private suspend fun getPermissionStatus(): FeinnPermissionStatus =
        suspendCancellableCoroutine { continuation ->
            UNUserNotificationCenter.currentNotificationCenter()
                .getNotificationSettingsWithCompletionHandler(
                    completionHandler = { uNNotificationSettings ->
                        if (uNNotificationSettings == null) {
                            // Resume with failure if notification settings are null
                            continuation.resumeWith(Result.failure(IllegalStateException("Notification settings is null")))
                            return@getNotificationSettingsWithCompletionHandler
                        }

                        // Map the authorization status to a FeinnPermissionStatus
                        val authorizeStatus = uNNotificationSettings.authorizationStatus
                        val feinnPermissionStatus = when (authorizeStatus) {
                            UNAuthorizationStatusNotDetermined -> FeinnPermissionStatus.Denied(false)
                            UNAuthorizationStatusDenied -> FeinnPermissionStatus.Denied(true)
                            UNAuthorizationStatusAuthorized, UNAuthorizationStatusProvisional, UNAuthorizationStatusEphemeral -> FeinnPermissionStatus.Granted
                            else -> FeinnPermissionStatus.Denied(false)
                        }

                        // Resume the coroutine with the mapped permission status
                        continuation.resumeWith(Result.success(feinnPermissionStatus))
                    }
                )

        }
}
