package id.feinn.components.screenshot

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.drawToBitmap


internal actual class FeinnTakeScreenshot(
    private val context: Context
) {
    @Composable
    public actual fun ScreenshotView(
        modifier: Modifier,
        content: @Composable () -> Unit,
    ): FeinnScreenshotView {
        val composeView = ComposeView(context).apply {
            setContent {
                Box(
                    modifier = modifier
                ) {
                    content()
                }
            }
        }

        return composeView
    }

    actual fun takeScreenshot(
        bitmapCallback: (FeinnScreenshotResult) -> Unit,
        size: CoordinateSize,
        screenshotState: FeinnScreenshotView
    ) {


        val widthPx = size.width.toInt()
        val heightPx = size.height.toInt()
        val widthSpec = View.MeasureSpec.makeMeasureSpec(widthPx, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(heightPx, View.MeasureSpec.EXACTLY)

        composeView.measure(widthSpec, heightSpec)
        composeView.layout(0, 0, widthPx, heightPx)

        val bitmap: Bitmap = composeView.drawToBitmap()
        val imageBitmap: ImageBitmap = bitmap.asImageBitmap()

        bitmapCallback(
            FeinnScreenshotResult.Success(imageBitmap)
        )
    }

}

@Composable
internal actual fun rememberFeinnTakeScreenshot(): FeinnTakeScreenshot {
    val context = LocalContext.current

    return remember { FeinnTakeScreenshot(context) }
}

internal actual typealias FeinnScreenshotView = ComposeView