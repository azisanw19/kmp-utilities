package id.feinn.utility.permission

import android.Manifest

/**
 * Represents platform-specific implementation of permission types.
 *
 * @param permission The platform-specific permission string, such as `Manifest.permission.CAMERA` on Android.
 */
public actual enum class FeinnPermissionType(
    public val permission: String
) {
    /**
     * Permission to access the device's camera.
     * Corresponds to `Manifest.permission.CAMERA` on Android.
     */
    Camera(Manifest.permission.CAMERA)
}
