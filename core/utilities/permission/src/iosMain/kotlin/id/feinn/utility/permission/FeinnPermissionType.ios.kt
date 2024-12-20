package id.feinn.utility.permission

import id.feinn.utility.permission.feature.FeinnMutablePermissionCaptureDevice
import id.feinn.utility.permission.feature.FeinnMutablePermissionUserNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.AVFoundation.AVMediaTypeVideo
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound

/**
 * Represents different types of permissions that can be managed within the application.
 *
 * Each enum entry corresponds to a specific type of permission and provides an implementation
 * for retrieving its mutable permission state. This abstraction allows for handling various
 * permission types in a structured and extensible manner.
 *
 * Enum Entries:
 * - `Camera`: Represents the camera permission, typically used for capturing photos or videos.
 *   Provides a mutable permission state specific to the camera device.
 *
 * Abstract Method:
 * - `getMutablePermission()`: Abstract method that must be implemented by each enum entry to
 *   return the corresponding `FeinnMutablePermissionState` object.
 *
 * Example Usage:
 * ```
 * val cameraPermission = FeinnPermissionType.Camera.getMutablePermission()
 * ```
 */
public actual enum class FeinnPermissionType {
    /**
     * Represents the camera permission.
     *
     * This entry manages permissions for accessing the camera device using `AVMediaTypeVideo`.
     */
    Camera {
        override fun getMutablePermission(): FeinnMutablePermissionState =
            FeinnMutablePermissionCaptureDevice(this, AVMediaTypeVideo)
    },

    /**
     * Represents the notification permission.
     *
     * This entry manages permissions for accessing notification-related features such as alerts,
     * badges, and sounds using `UNAuthorizationOption` settings.
     * It provides a mutable permission state for controlling notification access.
     */
    Notification {
        override fun getMutablePermission(): FeinnMutablePermissionState =
            FeinnMutablePermissionUserNotification(
                permission = this,
                options = UNAuthorizationOptionAlert
                        or UNAuthorizationOptionSound
                        or UNAuthorizationOptionBadge,
                dispatcher = Dispatchers.IO
            )
    };

    /**
     * Retrieves the mutable permission state associated with the permission type.
     *
     * This method must be implemented by all enum entries to provide the specific implementation
     * of `FeinnMutablePermissionState` for the permission type.
     *
     * @return A `FeinnMutablePermissionState` object representing the current permission type's state.
     */
    internal abstract fun getMutablePermission(): FeinnMutablePermissionState
}