package id.feinn.components.screenshot

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIGraphicsImageRenderer
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow


@Composable
public actual fun rememberFeinnTakeScreenshot(): FeinnTakeScreenshot = remember {
    FeinnTakeScreenshot()
}

public actual class FeinnTakeScreenshot {

    @Composable
    public actual fun ScreenshotView(
        modifier: Modifier,
        content: @Composable () -> Unit
    ): FeinnScreenshotView {
        val viewController = ComposeUIViewController {
            Box(modifier = modifier) {
                content()
            }
        }
        viewController.view

        return viewController
    }

    @OptIn(ExperimentalForeignApi::class)
    public actual fun takeScreenshot(
        bitmapCallback: (FeinnScreenshotResult) -> Unit,
        size: CoordinateSize,
        screenView: FeinnScreenshotView
    ) {
        val view = screenView.view
        view.setFrame(CGRectMake(0.0, 0.0, size.width, size.height))

        val window = UIWindow(frame = view.frame)
        window.rootViewController = screenView
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

public actual typealias FeinnScreenshotView = UIViewController