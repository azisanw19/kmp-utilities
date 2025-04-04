package id.feinn.utility.notification

/**
 * Represents the trigger conditions for when a Feinn notification should be delivered.
 *
 * This is a sealed class hierarchy that defines all possible notification trigger types.
 */
public sealed class FeinnNotificationTrigger {
    /**
     * A trigger that causes the notification to be delivered immediately.
     *
     * Usage example:
     * ```
     * notification.trigger = FeinnNotificationTrigger.Now
     * ```
     */
    public data object Now : FeinnNotificationTrigger()
}