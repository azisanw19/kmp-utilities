package id.feinn.utility.permission

import android.Manifest
import android.os.Build

/**
 * Represents platform-specific implementation of permission types.
 *
 * This enum encapsulates various permissions required by the application, providing
 * platform-specific mappings for each permission type. For instance, Android permissions
 * are represented using constants from the `Manifest.permission` class.
 *
 * @param permission The platform-specific permission string, such as `Manifest.permission.CAMERA` on Android.
 */
public actual enum class FeinnPermissionType(
    public val permission: String?
) {
    /**
     * Permission to access the device's camera.
     *
     * This permission allows the application to access the camera hardware on the device.
     * Corresponds to `Manifest.permission.CAMERA` on Android.
     */
    Camera(Manifest.permission.CAMERA),

    /**
     * Permission to send notifications to the user.
     *
     * On Android, this corresponds to `Manifest.permission.POST_NOTIFICATIONS` for devices
     * running Android 13 (API level 33) or higher. For older versions, this permission is not required.
     */
    Notification(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.POST_NOTIFICATIONS
        else null
    )
}
