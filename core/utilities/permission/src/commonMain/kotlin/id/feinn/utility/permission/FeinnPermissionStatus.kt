package id.feinn.utility.permission

/**
 * Represents the status of a permission.
 */
public sealed class FeinnPermissionStatus {

    /**
     * Indicates that the permission has been granted.
     */
    public data object Granted : FeinnPermissionStatus()

    /**
     * Indicates that the permission has been denied.
     *
     * @param shouldShowRationale A boolean indicating whether the user should be shown a rationale for the permission request.
     */
    public data class Denied(
        val shouldShowRationale: Boolean
    ) : FeinnPermissionStatus()
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
 * @return `true` if the permission is [FeinnPermissionStatus.Denied] and [shouldShowRationale] is true, otherwise `false`.
 */
public val FeinnPermissionStatus.shouldShowRationale: Boolean
    get() = when (this) {
        FeinnPermissionStatus.Granted -> false
        is FeinnPermissionStatus.Denied -> shouldShowRationale
    }