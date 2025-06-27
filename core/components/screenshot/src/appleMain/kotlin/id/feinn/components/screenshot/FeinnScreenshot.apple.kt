package id.feinn.components.screenshot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.UIApplication
import platform.UIKit.UIGraphicsImageRenderer
import platform.UIKit.UIGraphicsImageRendererFormat
import platform.UIKit.UIImage
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

internal actual class FeinnTakeScreenshot {

    @OptIn(ExperimentalForeignApi::class)
    actual fun takeScreenshot(
        bounds: Rect,
        bitmapCallback: (FeinnScreenshotResult) -> Unit
    ) {
        dispatch_async(dispatch_get_main_queue()) {
            val window = UIApplication.sharedApplication.keyWindow ?: run {
                bitmapCallback(FeinnScreenshotResult.Error(Exception("No key window available")))
                return@dispatch_async
            }
            try {
                val size = CGSizeMake(
                    bounds.width.toDouble(),
                    bounds.height.toDouble()
                )

                // TODO: make full view screenshot
                val renderer = UIGraphicsImageRenderer(
                    size = size,
                    format = UIGraphicsImageRendererFormat.defaultFormat().apply {
                        opaque = false
                        prefersExtendedRange = true
                    }
                )

                val image: UIImage = renderer.imageWithActions { context ->
                    window.layer.renderInContext(context?.CGContext())
                }

                val imageBitmap: ImageBitmap = image.toImageBitmap()

                bitmapCallback(FeinnScreenshotResult.Success(imageBitmap))
            } catch (e: Exception) {
                bitmapCallback(FeinnScreenshotResult.Error(e))
            }
        }
    }

}

@Composable
internal actual fun rememberFeinnTakeScreenshot(): FeinnTakeScreenshot = remember {
    FeinnTakeScreenshot()
}