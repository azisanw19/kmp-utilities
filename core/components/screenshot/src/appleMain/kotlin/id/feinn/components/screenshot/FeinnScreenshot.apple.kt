package id.feinn.components.screenshot

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIGraphicsImageRenderer
import platform.UIKit.UIWindow


@Composable
internal actual fun rememberFeinnTakeScreenshot(): FeinnTakeScreenshot = remember {
    FeinnTakeScreenshot()
}

internal actual class FeinnTakeScreenshot {
    @OptIn(ExperimentalForeignApi::class)
    actual fun takeScreenshot(
        modifier: Modifier,
        bitmapCallback: (FeinnScreenshotResult) -> Unit,
        size: CoordinateSize,
        content: @Composable () -> Unit,
    ) {
        val viewController = ComposeUIViewController {
            Box(modifier = modifier) {
                content()
            }

        }
        val view = viewController.view
        view.setFrame(CGRectMake(0.0, 0.0, size.width, size.height))

        val window = UIWindow(frame = view.frame)
        window.rootViewController = viewController
        window.makeKeyAndVisible()

        val renderer = UIGraphicsImageRenderer(bounds = view.bounds)
        val image = renderer.imageWithActions {
            view.drawViewHierarchyInRect(view.bounds, afterScreenUpdates = true)
        }

        bitmapCallback(
            FeinnScreenshotResult.Success(image.toImageBitmap())
        )
    }
}