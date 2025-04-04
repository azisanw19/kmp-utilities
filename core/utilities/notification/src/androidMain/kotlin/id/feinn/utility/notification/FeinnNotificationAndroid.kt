package id.feinn.utility.notification

/**
 * Android-specific notification configuration holder with singleton pattern.
 * Manages notification resources and configurations specific to Android platform.
 */
public class FeinnNotificationAndroid private constructor() {

    /**
     * Companion object providing singleton access to [FeinnNotificationAndroid] instance.
     */
    public companion object {
        private var INSTANCE: FeinnNotificationAndroid? = null

        /**
         * Gets the singleton instance of [FeinnNotificationAndroid].
         * Creates a new instance if one doesn't exist.
         *
         * @return The singleton [FeinnNotificationAndroid] instance
         */
        public fun getInstance(): FeinnNotificationAndroid {
            if (INSTANCE == null) {
                INSTANCE = FeinnNotificationAndroid()
            }
            return INSTANCE ?: FeinnNotificationAndroid()
        }
    }

    private var drawableId: Int? = null

    /**
     * Sets the drawable resource ID for notification icons.
     *
     * @param drawableId The resource ID of the drawable to use for notifications
     */
    public fun setDrawableId(drawableId: Int) {
        this.drawableId = drawableId
    }

    /**
     * Retrieves the configured notification drawable resource ID.
     *
     * @return The configured drawable resource ID
     * @throws IllegalArgumentException if drawableId hasn't been set (is null)
     */
    @Throws(IllegalArgumentException::class)
    public fun getDrawableId(): Int {
        checkNotNull(drawableId) { "drawableId cannot be null" }
        return drawableId!!
    }

}