package id.feinn.utility.notification.feature.manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import id.feinn.utility.notification.FeinnAndroidChannel
import kotlin.jvm.Throws

internal class FeinnNotificationManager(
    private val context: Context
) {

    var feinnAndroidChannel: FeinnAndroidChannel? = null
    var identifier: String? = null
    private var notificationManager: NotificationManager? = null

    @Throws(IllegalStateException::class)
    fun builder() : FeinnNotificationManager {
        checkNotNull(feinnAndroidChannel) { "Channel cannot be null" }
        checkNotNull(identifier) { "identifier cannot be null" }

        createNotificationChannel()

        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        return this
    }

    @Throws(IllegalStateException::class)
    fun build(): NotificationManager {
        checkNotNull(notificationManager) { "notificationManager cannot be null" }

        return notificationManager!!
    }

    @Throws(IllegalStateException::class)
    private fun createNotificationChannel() {
        checkNotNull(feinnAndroidChannel) { "Channel cannot be null" }
        checkNotNull(identifier) { "identifier cannot be null" }

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                feinnAndroidChannel?.id ?: identifier,
                feinnAndroidChannel!!.name,
                importance
            )

            if (feinnAndroidChannel!!.description != null)
                channel.description = feinnAndroidChannel!!.description

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}

internal fun feinnNotificationManager(
    context: Context,
    block: FeinnNotificationManager.() -> Unit
) : FeinnNotificationManager {
    val manager = FeinnNotificationManager(
        context = context
    )
    manager.block()
    return manager.builder()
}