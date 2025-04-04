package id.feinn.utility.notification.feature

import android.content.Context
import id.feinn.utility.notification.FeinnMutableNotificationState
import id.feinn.utility.notification.feature.content.FeinnNotificationContent
import id.feinn.utility.notification.feature.content.feinnNotificationContent
import id.feinn.utility.notification.feature.manager.FeinnNotificationManager
import id.feinn.utility.notification.feature.manager.feinnNotificationManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Android-specific implementation of [FeinnMutableNotificationState] that handles
 * actual notification creation and display using Android's notification system.
 *
 * This class:
 * - Manages notification building and display on Android
 * - Handles coroutine-based asynchronous notification sending
 * - Validates required notification parameters
 * - Integrates with Android's notification channels (API 26+)
 *
 * @property context The Android [Context] used for notification services
 * @property dispatcher The [CoroutineDispatcher] used for background operations
 */
internal class FeinnNotificationCompat(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
) : FeinnMutableNotificationState() {

    private val coroutineScope = CoroutineScope(dispatcher)

    /**
     * Sends the notification asynchronously using the configured coroutine dispatcher.
     *
     * The notification will be:
     * 1. Validated for required parameters
     * 2. Built according to current state
     * 3. Displayed immediately (using [FeinnNotificationTrigger.Now])
     */
    override fun send() {
        coroutineScope.launch {
            sendNotificationNow()
        }
    }

    /**
     * Handles immediate notification display on Android.
     *
     * @throws IllegalStateException if required parameters (identifier, channel) are not set
     */
    private fun sendNotificationNow() {
        val notificationManager = builderNotificationManager().build()
        val content = builderContent().build()

        notificationManager.notify(0, content)
    }

    /**
     * Builds the notification content with current configuration.
     *
     * @return Configured [FeinnNotificationContent] builder
     * @throws IllegalStateException if:
     *   - [identifier] is null
     *   - [androidChannel] or its id is null
     */
    @Throws(IllegalStateException::class)
    private fun builderContent(): FeinnNotificationContent {
        checkNotNull(identifier) { "identifier cannot be null" }
        checkNotNull(androidChannel?.id) { "androidChannel cannot be null" }

        val notification = feinnNotificationContent(
            context = context,
            channelId = this@FeinnNotificationCompat.androidChannel!!.id!!,
        ) {
            notificationData = this@FeinnNotificationCompat.data
        }

        return notification
    }

    /**
     * Builds the notification manager with current configuration.
     *
     * @return Configured [FeinnNotificationManager] builder
     * @throws IllegalStateException if [identifier] is null
     */
    @Throws(IllegalStateException::class)
    private fun builderNotificationManager(): FeinnNotificationManager {
        checkNotNull(identifier) { "identifier cannot be null" }

        val manager = feinnNotificationManager(context) {
            feinnAndroidChannel = this@FeinnNotificationCompat.androidChannel
            identifier = this@FeinnNotificationCompat.identifier
        }

        return manager
    }

}