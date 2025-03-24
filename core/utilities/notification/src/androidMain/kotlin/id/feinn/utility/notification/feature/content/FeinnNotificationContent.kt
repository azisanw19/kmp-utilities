package id.feinn.utility.notification.feature.content

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import id.feinn.utility.notification.FeinnNotificationAndroid
import id.feinn.utility.notification.FeinnNotificationData

internal class FeinnNotificationContent(
    private val context: Context,
    private val channelId: String
) {

    var notificationData: FeinnNotificationData? = null
    private var notificationContent: Notification? = null

    fun builder() : FeinnNotificationContent {
        val notificationBuilder =  NotificationCompat.Builder(context, channelId)
        val feinnNotificationAndroid = FeinnNotificationAndroid.getInstance()

        notificationData?.title?.let { title ->
            notificationBuilder.setContentTitle(title)
        }

        notificationData?.body?.let { body ->
            notificationBuilder.setContentText(body)
        }

        notificationBuilder.setSmallIcon(feinnNotificationAndroid.getDrawableId())

        notificationContent = notificationBuilder.build()

        return this
    }

    @Throws(IllegalArgumentException::class)
    fun build(): Notification {
        checkNotNull(notificationContent) { "notification content cannot be null" }

        return notificationContent!!
    }

}

internal fun feinnNotificationContent(
    context: Context,
    channelId: String,
    block: FeinnNotificationContent.() -> Unit
) : FeinnNotificationContent {
    val content = FeinnNotificationContent(
        context = context,
        channelId = channelId
    )
    content.block()
    return content.builder();
}