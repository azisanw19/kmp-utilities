package id.feinn.utility.notification

import androidx.compose.runtime.Composable
import id.feinn.utility.context.FeinnLocalContext

@Composable
public expect fun rememberFeinnNotification(
    isPermissionGranted: (Boolean) -> Unit = {}
) : FeinnNotificationState

public interface FeinnNotificationState {

    public companion object {}

    public var data: FeinnNotificationData?
    public var trigger: FeinnNotificationTrigger
    public var identifier: String?
    public var androidChannel: FeinnAndroidChannel?

    public fun send()

}

public expect fun FeinnNotificationState.Companion.build(context: FeinnLocalContext, block: FeinnNotificationState.() -> Unit) : FeinnNotificationState