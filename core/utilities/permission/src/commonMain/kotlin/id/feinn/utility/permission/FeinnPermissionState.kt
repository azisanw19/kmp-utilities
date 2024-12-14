package id.feinn.utility.permission

import androidx.compose.runtime.Composable

/**
 * Remembers and provides a [FeinnPermissionState] instance for a given [permission].
 *
 * @param permission The permission type that is being requested. Should be of type [FeinnPermissionType].
 * @param onPermissionResult A callback that is invoked when the permission result is obtained.
 * The callback provides a [Boolean] indicating whether the permission was granted (true) or denied (false).
 * @return A [FeinnPermissionState] instance associated with the given [permission].
 */
@Composable
public expect fun rememberFeinnPermissionState(
    permission: FeinnPermissionType,
    onPermissionResult: (Boolean) -> Unit = {}
): FeinnPermissionState

/**
 * Represents the state of a specific permission.
 */
public interface FeinnPermissionState {

    /**
     * The type of permission represented by this state.
     */
    public val permission: FeinnPermissionType

    /**
     * The current status of the permission.
     * This is of type [FeinnPermissionStatus], which can represent granted, denied, or other statuses.
     */
    public var status: FeinnPermissionStatus

    /**
     * Launches a request for the associated permission.
     * This method is typically used to prompt the user for permission.
     */
    public fun launchPermissionRequest()

    /**
     * Launches the device settings screen for the user to manually change the permission state.
     * This is useful when the user has permanently denied the permission or needs to enable it via settings.
     */
    public fun launchSettingRequest()
}
