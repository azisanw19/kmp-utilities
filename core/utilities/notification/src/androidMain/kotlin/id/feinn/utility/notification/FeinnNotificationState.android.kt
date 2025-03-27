package id.feinn.utility.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import id.feinn.utility.context.FeinnLocalContext
import id.feinn.utility.notification.feature.FeinnNotificationCompat
import kotlinx.coroutines.Dispatchers

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

public actual fun FeinnNotificationState.Companion.build(context: FeinnLocalContext,block: FeinnNotificationState.() -> Unit): FeinnNotificationState {
    val state = FeinnNotificationCompat(
        context = context,
        dispatcher = Dispatchers.IO
    )
    state.block()
    return state
}