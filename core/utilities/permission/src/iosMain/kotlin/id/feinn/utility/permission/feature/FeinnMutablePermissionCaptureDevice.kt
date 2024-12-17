package id.feinn.utility.permission.feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import id.feinn.utility.permission.FeinnMutablePermissionState
import id.feinn.utility.permission.FeinnPermissionStatus
import id.feinn.utility.permission.FeinnPermissionType
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType

/**
 * A mutable permission state implementation for managing access to media capture devices, such as
 * cameras or microphones, using `AVCaptureDevice` from Apple's AVFoundation framework.
 *
 * @param permission The type of permission being managed. This corresponds to a `FeinnPermissionType`.
 * @param mediaType The media type for which permission is requested, such as `AVMediaTypeVideo`
 *                  or `AVMediaTypeAudio`. This parameter must not be null.
 *
 * @throws IllegalArgumentException if `mediaType` is null.
 *
 * Properties:
 * - `status`: Represents the current permission status as a `FeinnPermissionStatus`. Automatically updated
 *   when the permission status changes.
 * - `launcher`: A lambda invoked with the result of a permission request (`true` for granted, `false` for denied).
 *
 * Methods:
 * - `refreshPermissionStatus()`: Checks and updates the current permission status by querying the
 *   `AVCaptureDevice` API.
 * - `launchPermissionRequest()`: Requests access to the media type and invokes the `launcher` callback
 *   with the result.
 *
 * Private Method:
 * - `getPermissionStatus()`: Queries the current authorization status for the specified media type using
 *   `AVCaptureDevice.authorizationStatusForMediaType`.
 *
 * Example Usage:
 * ```
 * val cameraPermissionState = FeinnMutablePermissionCaptureDevice(
 *     permission = FeinnPermissionType.Camera,
 *     mediaType = AVMediaTypeVideo
 * )
 *
 * cameraPermissionState.launchPermissionRequest()
 * ```
 *
 * See Also:
 * [AVCaptureDevice Documentation](https://developer.apple.com/documentation/avfoundation/avcapturedevice)
 */
internal class FeinnMutablePermissionCaptureDevice(
    override val permission: FeinnPermissionType,
    private val mediaType: String?
) : FeinnMutablePermissionState() {

    init {
        mediaType ?: throw IllegalArgumentException("mediaType cannot be null")
    }

    /**
     * The current permission status, automatically updated when refreshed or requested.
     */
    override var status: FeinnPermissionStatus by mutableStateOf(getPermissionStatus())

    /**
     * A lambda invoked with the result of a permission request.
     * - `true` if the permission was granted.
     * - `false` if the permission was denied.
     */
    override var launcher: ((Boolean) -> Unit)? = null

    /**
     * Refreshes the permission status by querying the current state from `AVCaptureDevice`.
     */
    override fun refreshPermissionStatus() {
        status = getPermissionStatus()
    }

    /**
     * Launches a request for the specified media type's permission.
     *
     * The `completionHandler` of `AVCaptureDevice.requestAccessForMediaType` will invoke the
     * `launcher` callback with the result.
     */
    override fun launchPermissionRequest() {
        AVCaptureDevice.requestAccessForMediaType(
            mediaType = mediaType,
            completionHandler = {
                launcher?.invoke(it)
            }
        )
    }

    /**
     * Queries the current authorization status for the specified media type.
     *
     * @return The corresponding `FeinnPermissionStatus` based on the authorization status.
     */
    private fun getPermissionStatus(): FeinnPermissionStatus {
        val authorizeStatus = AVCaptureDevice.authorizationStatusForMediaType(mediaType)
        return when (authorizeStatus) {
            AVAuthorizationStatusNotDetermined -> FeinnPermissionStatus.Denied(false)
            AVAuthorizationStatusRestricted, AVAuthorizationStatusDenied -> FeinnPermissionStatus.Denied(true)
            AVAuthorizationStatusAuthorized -> FeinnPermissionStatus.Granted
            else -> FeinnPermissionStatus.Denied(false)
        }
    }
}
