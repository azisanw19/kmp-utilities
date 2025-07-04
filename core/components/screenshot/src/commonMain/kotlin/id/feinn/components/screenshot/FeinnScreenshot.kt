package id.feinn.components.screenshot

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity

@Composable
public fun FeinnScreenshot(
    modifier: Modifier = Modifier,
    screenshotState: FeinnScreenshotState,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    var coordinateSize by remember { mutableStateOf<CoordinateSize?>(null) }
    val feinnTakeScreenshot = rememberFeinnTakeScreenshot()
    val screenView = feinnTakeScreenshot.ScreenshotView(
        modifier = modifier,
        content = content
    )

    DisposableEffect(Unit) {
        screenshotState.callback = {
            if (coordinateSize != null) {
                feinnTakeScreenshot.takeScreenshot(
                    bitmapCallback = {
                        screenshotState.imageBitmap = when (it) {
                            is FeinnScreenshotResult.Success -> {
                                it.bitmap
                            }
                            else -> {
                                error("bitmapCallback is not success")
                            }
                        }
                    },
                    size = coordinateSize!!,
                    screenView = screenView
                )
            } else {
                error("coordinateSize is null")
            }
        }

        onDispose {
            screenshotState.callback = null
            screenshotState.imageBitmap = null
        }
    }

    Box(modifier = modifier
        .onGloballyPositioned { coordinates ->
            val height = with(density) { coordinates.size.height.toDp() }
            val width = with(density) { coordinates.size.width.toDp() }

            coordinateSize = CoordinateSize(
                height = height.value.toDouble(),
                width = width.value.toDouble()
            )
        }
    ) {
        content()
    }
}

public data class CoordinateSize(
    val width: Double,
    val height: Double
)

public expect class FeinnTakeScreenshot {

    @Composable
    public fun ScreenshotView(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ): FeinnScreenshotView

    public fun takeScreenshot(
        bitmapCallback: (FeinnScreenshotResult) -> Unit,
        size: CoordinateSize,
        screenView: FeinnScreenshotView
    )

}

@Composable
public expect fun rememberFeinnTakeScreenshot() : FeinnTakeScreenshot

public expect class FeinnScreenshotView