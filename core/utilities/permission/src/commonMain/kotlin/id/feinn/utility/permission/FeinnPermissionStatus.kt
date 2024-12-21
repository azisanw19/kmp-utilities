package id.feinn.utility.permission

/**
 * Represents the status of a permission.
 *
 * This sealed class defines the various states a permission can have, providing clarity
 * about whether a permission is granted, denied, or in an unknown state.
 */
public sealed class FeinnPermissionStatus {

    /**
     * Indicates that the permission has been granted.
     *
     * When this status is returned, the application can safely use the functionality
     * associated with the permission.
     */
    public data object Granted : FeinnPermissionStatus()

    /**
     * Indicates that the permission has been denied.
     *
     * @param shouldShowRationale A boolean indicating whether the user should be shown a rationale
     * for the permission request. This is typically true if the user previously denied the permission
     * but did not select "Don't ask again."
     */
    public data class Denied(
        val shouldShowRationale: Boolean
    ) : FeinnPermissionStatus()

    /**
     * Indicates that the status of the permission is unknown.
     *
     * This status might occur in scenarios where the permission state has not been determined yet.
     */
    public data object Unknown : FeinnPermissionStatus()
}

/**
 * Extension property to check if the permission status is granted.
 *
 * @return `true` if the permission is [FeinnPermissionStatus.Granted], otherwise `false`.
 */
public val FeinnPermissionStatus.isGranted: Boolean
    get() = this == FeinnPermissionStatus.Granted

/**
 * Extension property to determine if a rationale should be shown for the denied permission.
 *
 * This property evaluates the current [FeinnPermissionStatus] and determines whether a rationale
 * should be shown to the user. Typically, a rationale is shown when the user previously denied
 * the permission but did not select "Don't ask again."
 *
 * @return `true` if the permission is [FeinnPermissionStatus.Denied] and [shouldShowRationale] is true, otherwise `false`.
 */
public val FeinnPermissionStatus.shouldShowRationale: Boolean
    get() = when (this) {
        FeinnPermissionStatus.Granted -> false
        is FeinnPermissionStatus.Denied -> shouldShowRationale
        is FeinnPermissionStatus.Unknown -> true
    }
