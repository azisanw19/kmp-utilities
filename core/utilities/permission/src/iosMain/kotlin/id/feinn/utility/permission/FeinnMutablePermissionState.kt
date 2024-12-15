package id.feinn.utility.permission

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

/**
 * An abstract class representing the mutable state of a permission.
 *
 * This class extends `FeinnPermissionState` and provides additional functionality
 * to manage and update permission states dynamically. It also includes the ability
 * to launch the app's settings screen for the user to modify permissions.
 *
 * Abstract Properties:
 * - `launcher`: A nullable lambda that handles the result of a permission request.
 *   It takes a `Boolean` parameter where `true` indicates the permission was granted,
 *   and `false` indicates it was denied.
 *
 * Abstract Methods:
 * - `refreshPermissionStatus()`: Updates the current status of the permission by checking
 *   its latest state. This method must be implemented by subclasses to define how
 *   the status should be refreshed.
 *
 * Overridden Methods:
 * - `launchSettingRequest()`: Opens the app's settings screen to allow the user to modify
 *   the app's permissions. It utilizes `UIApplicationOpenSettingsURLString` to create the
 *   settings URL and opens it using `UIApplication.sharedApplication.openURL()`.
 *
 * Example Usage (in a subclass):
 * ```
 * class CameraPermissionState : FeinnMutablePermissionState() {
 *     override var launcher: ((Boolean) -> Unit)? = null
 *
 *     override fun refreshPermissionStatus() {
 *         // Implementation to check and update the camera permission status.
 *     }
 * }
 * ```
 *
 * Notes:
 * - Ensure that the `UIApplicationOpenSettingsURLString` is valid before invoking
 *   `launchSettingRequest()`. If invalid, an exception is thrown.
 */
internal abstract class FeinnMutablePermissionState : FeinnPermissionState {

    /**
     * A nullable lambda to handle the result of a permission request.
     * Should be assigned a function that processes the permission result
     * (`true` for granted, `false` for denied).
     */
    abstract var launcher: ((Boolean) -> Unit)?

    abstract fun refreshPermissionStatus()

    override fun launchSettingRequest() {
        val nsUrl = checkNotNull(
            value = NSURL.URLWithString(UIApplicationOpenSettingsURLString),
            lazyMessage = {
                "$UIApplicationOpenSettingsURLString is not a valid URL."
            }
        )

        UIApplication.sharedApplication.openURL(
            url = nsUrl
        )
    }
}