package id.feinn.components.screenshot

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
public fun FeinnScreenshot(
    modifier: Modifier = Modifier,
    screenshotState: FeinnScreenshotState,
    content: @Composable () -> Unit
) {
    val feinnTakeScreenshot = rememberFeinnTakeScreenshot()

    var composableBounds by remember {
        mutableStateOf<Rect?>(null)
    }

    DisposableEffect(Unit) {
        screenshotState.callback = {
            composableBounds?.let { bounds ->
                if (bounds.width == 0f || bounds.height == 0f) return@let

                feinnTakeScreenshot.takeScreenshot(bounds) { imageResult: FeinnScreenshotResult ->
                    screenshotState.imageState.value = imageResult

                    when (imageResult) {
                        is FeinnScreenshotResult.Success -> {
                            screenshotState.imageBitmapState.value = imageResult.bitmap
                        }

                        is FeinnScreenshotResult.Error -> {
                            println(imageResult.throwable.message)
                            println("Error: ${imageResult.throwable.message}")
                            screenshotState.imageBitmapState.value = null
                        }

                        FeinnScreenshotResult.Initial -> {}
                    }
                }
            }
        }

        onDispose {
            screenshotState.callback = null
            screenshotState.imageBitmapState.value = null
        }
    }

    Box(modifier = modifier
        .onGloballyPositioned { coordinates ->
            composableBounds = coordinates.boundsInRoot()
        }
    ) {
        content()
    }
}

internal expect class FeinnTakeScreenshot {
    fun takeScreenshot(bounds: Rect, bitmapCallback: (FeinnScreenshotResult) -> Unit)
}

@Composable
internal expect fun rememberFeinnTakeScreenshot() : FeinnTakeScreenshot