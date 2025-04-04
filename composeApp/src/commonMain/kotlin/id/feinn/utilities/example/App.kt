package id.feinn.utilities.example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import id.feinn.utility.context.FeinnLocalPlatformContext
import id.feinn.utility.launcher.rememberFeinnLauncer
import id.feinn.utility.notification.FeinnAndroidChannel
import id.feinn.utility.notification.FeinnNotificationData
import id.feinn.utility.notification.rememberFeinnNotification
import id.feinn.utility.permission.FeinnPermissionStatus
import id.feinn.utility.permission.FeinnPermissionType
import id.feinn.utility.permission.rememberFeinnPermissionState
import id.feinn.utility.permission.shouldShowRationale

@Composable
fun App() {
    MaterialTheme {
        val context = FeinnLocalPlatformContext.current
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
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
        }
    }
}