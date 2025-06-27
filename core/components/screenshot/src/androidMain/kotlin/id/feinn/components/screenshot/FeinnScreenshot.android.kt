package id.feinn.components.screenshot

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalView

internal actual class FeinnTakeScreenshot(
    private val view: View
) {
    actual fun takeScreenshot(
        bounds: Rect,
        bitmapCallback: (FeinnScreenshotResult) -> Unit
    ) {
        view.screenshot(bounds, bitmapCallback)
    }
}

@Composable
internal actual fun rememberFeinnTakeScreenshot(): FeinnTakeScreenshot {
    val view = LocalView.current

    return remember { FeinnTakeScreenshot(view) }
}