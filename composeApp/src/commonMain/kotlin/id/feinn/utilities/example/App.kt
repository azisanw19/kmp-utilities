package id.feinn.utilities.example

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import id.feinn.components.screenshot.FeinnScreenshot
import id.feinn.components.screenshot.rememberFeinnScreenshotState
import id.feinn.utility.context.FeinnLocalPlatformContext
import id.feinn.utility.launcher.rememberFeinnLauncer
import id.feinn.utility.notification.FeinnAndroidChannel
import id.feinn.utility.notification.FeinnNotificationData
import id.feinn.utility.notification.rememberFeinnNotification
import id.feinn.utility.permission.FeinnPermissionStatus
import id.feinn.utility.permission.FeinnPermissionType
import id.feinn.utility.permission.rememberFeinnPermissionState
import id.feinn.utility.permission.shouldShowRationale
import kotlinx.coroutines.delay

@Composable
fun App() {
    MaterialTheme {
        val context = FeinnLocalPlatformContext.current
        val verticalScrollState = rememberScrollState()
        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(verticalScrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val launcher = rememberFeinnLauncer()

            Button(
                onClick = {
                    launcher.launch("https://www.google.com")
                }
            ) {
                Text("Open Google")
            }
            Button(
                onClick = {
                    launcher.launch("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
                }
            ) {
                Text("Open pdf file")
            }
            val permissionCamera = rememberFeinnPermissionState(
                permission = FeinnPermissionType.Camera,
                onPermissionResult = {
                    println("Permission result: $it")
                }
            )
            Text(
                "Permission Camera: ${permissionCamera.status}"
            )
            Button(
                onClick = {
                    when (permissionCamera.status) {
                        FeinnPermissionStatus.Granted -> {
                            println("permissionCamera.status: ${permissionCamera.status}")
                        }
                        is FeinnPermissionStatus.Denied -> {
                            if (permissionCamera.status.shouldShowRationale) {
                                permissionCamera.launchSettingRequest()
                            } else {
                                permissionCamera.launchPermissionRequest()
                            }
                        }

                        FeinnPermissionStatus.Unknown -> {
                            println("permissionCamera.status: ${permissionCamera.status}")
                        }
                    }
                }
            ) {
                Text(
                    text = "Request Camera Permission"
                )
            }

            val permissionNotification = rememberFeinnPermissionState(
                permission = FeinnPermissionType.Notification,
                onPermissionResult = {
                    println("Permission result: $it")
                }
            )
            Text(
                "Permission Notification: ${permissionNotification.status}"
            )

            Button(
                onClick = {
                    when(permissionNotification.status) {
                        FeinnPermissionStatus.Granted -> {
                            println("permissionNotification.status: ${permissionNotification.status}")
                        }
                        is FeinnPermissionStatus.Denied -> {
                            if (permissionNotification.status.shouldShowRationale) {
                                permissionNotification.launchSettingRequest()
                            } else {
                                permissionNotification.launchPermissionRequest()
                            }
                        }
                        FeinnPermissionStatus.Unknown -> {
                            println("permissionNotification.status: ${permissionNotification.status}")
                        }
                    }
                }
            ) {
                Text("Request Notification Permission")
            }

            val notification = rememberFeinnNotification(
                isPermissionGranted = {
                    println("isPermissionGranted: $it")
                }
            )

            Button(
                onClick = {
                    notification.apply {
                        data = FeinnNotificationData(
                            title = "Feinn Notification title",
                            body = "Feinn Notification body"
                        )
                        identifier = "com.feinn.azisanw19"
                        androidChannel = FeinnAndroidChannel(
                            id = "testId",
                            name = "testName",
                            description = "testDescription"
                        )
                    }
                    notification.send()
                }
            ) {
                Text("Send Notification")
            }

            val screenshotState = rememberFeinnScreenshotState()
            var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

            Button(
                onClick = {
                    screenshotState.capture()
                    imageBitmap = screenshotState.imageBitmap
                    println("imageBitmap: $imageBitmap")
                }
            ) {
                Text("Take Screenshot")
            }

            imageBitmap?.let { bmp ->
                Text("The image exist")
                Image(
                    bitmap = bmp,
                    modifier = Modifier,
                    contentDescription = null
                )
            }

            val visibleItems = remember { mutableStateListOf<Int>() }

            LaunchedEffect(Unit) {
                repeat(100) { index ->
                    delay(100) // Delay antara item muncul (misalnya 10ms)
                    visibleItems.add(index)
                }
            }

            FeinnScreenshot(
                screenshotState = screenshotState
            ) {
                Column {
                    repeat(100) { index ->
                        AnimatedVisibility(visible = visibleItems.contains(index)) {
                            Text(text = "Item ke-$index")
                        }
                    }
                }
            }
        }
    }
}