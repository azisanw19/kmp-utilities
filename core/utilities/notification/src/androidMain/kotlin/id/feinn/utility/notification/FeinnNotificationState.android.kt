package id.feinn.utility.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import id.feinn.utility.context.FeinnLocalContext
import id.feinn.utility.notification.feature.FeinnNotificationCompat
import kotlinx.coroutines.Dispatchers

/**
 * Android-specific implementation of [rememberFeinnNotification] composable.
 * Creates and remembers a notification state tied to the Android context.
 *
 * @param isPermissionGranted Callback that receives the current notification permission state.
 *                            Called with `true` if permissions are granted, `false` otherwise.
 *                            The callback is automatically re-invoked when permissions change.
 * @return [FeinnNotificationState] instance configured with Android-specific behavior.
 */
@Composable
public actual fun rememberFeinnNotification(
    isPermissionGranted: (Boolean) -> Unit
): FeinnNotificationState {
    val context by rememberUpdatedState(LocalContext.current)

    val state = remember {
        FeinnNotificationCompat(
            context = context,
            dispatcher = Dispatchers.IO,
        )
    }

    state.checkPermissionState(
        isPermissionGranted = isPermissionGranted
    )

    return state

}

/**
 * Android-specific implementation for building a [FeinnNotificationState].
 * Creates a new notification state instance with Android context and applies configuration.
 *
 * @param context The Android context wrapper for notification operations.
 * @param block Configuration lambda to initialize the notification properties.
 * @return Configured [FeinnNotificationState] instance ready for use.
 */
public actual fun FeinnNotificationState.Companion.build(context: FeinnLocalContext,block: FeinnNotificationState.() -> Unit): FeinnNotificationState {
    val state = FeinnNotificationCompat(
        context = context,
        dispatcher = Dispatchers.IO
    )
    state.block()
    return state
}