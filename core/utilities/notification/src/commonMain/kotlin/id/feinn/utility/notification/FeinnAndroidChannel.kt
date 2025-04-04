package id.feinn.utility.notification

/**
 * Represents an Android channel configuration for Feinn.
 *
 * @property id The unique identifier for the channel. Can be null for default channels.
 * @property name The display name of the channel. This is required and shown to users.
 * @property description Optional description of the channel's purpose.
 *                       Displayed to users in channel settings (if supported by platform).
 */
public data class FeinnAndroidChannel(
    // default identifier
    val id: String? = null,
    val name: String,
    val description: String? = null,
)