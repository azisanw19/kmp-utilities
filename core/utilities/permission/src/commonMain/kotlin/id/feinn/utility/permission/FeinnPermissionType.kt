package id.feinn.utility.permission

/**
 * Represents the types of permissions that can be requested in the application.
 */
public expect enum class FeinnPermissionType {

    /**
     * Permission to access the device's camera.
     *
     * This permission allows the application to access the camera hardware on the device.
     */
    Camera,

    /**
     * Permission to send notifications to the user.
     */
    Notification
}