package id.feinn.utility.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import id.feinn.utility.context.FeinnLocalContext
import id.feinn.utility.notification.feature.FeinnNotificationCenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
/**
 * iOS-specific implementation of [rememberFeinnNotification] composable.
 * Creates and remembers the notification state for iOS platform.
 *
 * @param isPermissionGranted Callback that receives the current notification permission state.
 *                            Called with `true` if permissions are granted, `false` otherwise.
 *                            The callback is automatically re-invoked when permissions change.
 * @return [FeinnNotificationState] instance configured with iOS-specific behavior.
 */
@Composable
public actual fun rememberFeinnNotification(
    isPermissionGranted: (Boolean) -> Unit
): FeinnNotificationState {
    val state = remember {
        FeinnNotificationCenter(
            dispatcher = Dispatchers.IO
        )
    }

    state.checkPermissionState(
        isPermissionGranted = isPermissionGranted
    )

    return state
}

/**
 * iOS-specific implementation for building a [FeinnNotificationState].
 * Creates a new notification state instance with iOS-specific configuration.
 *
 * @param context The local context wrapper for iOS notification operations (unused on iOS).
 * @param block Configuration lambda to initialize the notification properties.
 * @return Configured [FeinnNotificationState] instance ready for use.
 */
public actual fun FeinnNotificationState.Companion.build(
    context: FeinnLocalContext,
    block: FeinnNotificationState.() -> Unit
): FeinnNotificationState {
    val state = FeinnNotificationCenter(
        dispatcher = Dispatchers.IO
    )
    state.block()
    return state
}