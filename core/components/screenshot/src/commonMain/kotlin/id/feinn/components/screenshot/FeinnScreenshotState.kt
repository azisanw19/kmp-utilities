package id.feinn.components.screenshot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap

public sealed class FeinnScreenshotResult {
    public data class Success internal constructor(val bitmap: ImageBitmap): FeinnScreenshotResult()
    public data class Error internal constructor(val throwable: Throwable): FeinnScreenshotResult()
}

@Composable
public fun rememberFeinnScreenshotState(): FeinnScreenshotState = remember {
    FeinnScreenshotState()
}

public class FeinnScreenshotState internal constructor() {
    internal var callback: (() -> Unit)? = null

    public fun capture() {
        callback?.invoke()
    }

    public var imageBitmap: ImageBitmap? = null
}