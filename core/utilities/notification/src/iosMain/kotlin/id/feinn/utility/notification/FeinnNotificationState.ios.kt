package id.feinn.utility.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import id.feinn.utility.context.FeinnLocalContext
import id.feinn.utility.notification.feature.FeinnNotificationCenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Composable
public actual fun rememberFeinnNotification(
    isPermissionGranted: (Boolean) -> Unit
) : FeinnNotificationState {
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

public actual fun FeinnNotificationState.Companion.build(context: FeinnLocalContext, block: FeinnNotificationState.() -> Unit) : FeinnNotificationState {
    val state = FeinnNotificationCenter(
        dispatcher = Dispatchers.IO
    )
    state.block()
    return state
}