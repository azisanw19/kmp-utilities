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

internal class FeinnNotificationCompat(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
) : FeinnMutableNotificationState() {

    private val coroutineScope = CoroutineScope(dispatcher)

    override fun send() {
        coroutineScope.launch {
            sendNotificationNow()
        }
    }

    private fun sendNotificationNow() {
        val notificationManager = builderNotificationManager().build()
        val content = builderContent().build()

        notificationManager.notify(0, content)
    }

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