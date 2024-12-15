package id.feinn.utility.permission

import id.feinn.utility.permission.feature.FeinnMutablePermissionCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo

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
    };

    /**
     * Retrieves the mutable permission state associated with the permission type.
     *
     * This method must be implemented by all enum entries to provide the specific implementation
     * of `FeinnMutablePermissionState` for the permission type.
     *
     * @return A `FeinnMutablePermissionState` object representing the current permission type's state.
     */
    internal abstract fun getMutablePermission() : FeinnMutablePermissionState
}