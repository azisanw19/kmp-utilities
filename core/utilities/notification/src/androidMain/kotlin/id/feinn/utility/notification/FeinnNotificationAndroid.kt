package id.feinn.utility.notification

public class FeinnNotificationAndroid private constructor() {

    public companion object {
        private var INSTANCE: FeinnNotificationAndroid? = null

        public fun getInstance(): FeinnNotificationAndroid {
            if (INSTANCE == null) {
                INSTANCE = FeinnNotificationAndroid()
            }
            return INSTANCE ?: FeinnNotificationAndroid()
        }
    }

    private var drawableId: Int? = null

    public fun setDrawableId(drawableId: Int) {
        this.drawableId = drawableId
    }

    @Throws(IllegalArgumentException::class)
    public fun getDrawableId(): Int {
        checkNotNull(drawableId) { "drawableId cannot be null" }

        return drawableId!!
    }

}