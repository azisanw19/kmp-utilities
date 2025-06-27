package id.feinn.components.screenshot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap

public sealed class FeinnScreenshotResult {
    public data object Initial: FeinnScreenshotResult()
    public data class Success internal constructor(val bitmap: ImageBitmap): FeinnScreenshotResult()
    public data class Error internal constructor(val throwable: Throwable): FeinnScreenshotResult()
}

public data class ScreenshotOptions(
    val width: Int? = null,
    val height: Int? = null
)

@Composable
public fun rememberFeinnScreenshotState(): FeinnScreenshotState = remember {
    FeinnScreenshotState()
}

public class FeinnScreenshotState internal constructor(
    private val timeInMillis: Long = 20,
) {
    public val imageState: MutableState<FeinnScreenshotResult> = mutableStateOf(FeinnScreenshotResult.Initial)

    public val imageBitmapState: MutableState<ImageBitmap?> = mutableStateOf(null)

    internal var callback: (() -> Unit)? = null

    public fun capture() {
        callback?.invoke()
    }

    /**
     * TODO: Optimize
     *
     * Code:
     * ```kotlin
     *     public val liveScreenshotFlow: Flow<ImageBitmap> = flow {
     *         while (true) {
     *             callback?.invoke()
     *             delay(timeInMillis)
     *             imageBitmapState.value?.let {
     *                 emit(it)
     *             }
     *         }
     *     }
     *         .flowOn(Dispatchers.Default)
     * ```
     */


    public val imageBitmap: ImageBitmap?
        get() = imageBitmapState.value
}