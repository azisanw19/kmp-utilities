package id.feinn.utilities.example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.feinn.utility.context.FeinnLocalPlatformContext
import id.feinn.utility.launcher.rememberFeinnLauncer
import id.feinn.utility.permission.FeinnPermissionStatus
import id.feinn.utility.permission.FeinnPermissionType
import id.feinn.utility.permission.rememberFeinnPermissionState
import id.feinn.utility.permission.shouldShowRationale
import id.feinn.utility.time.FeinnDate
import id.feinn.utility.time.FeinnDateTime
import id.feinn.utility.time.getFormattedDateTime
import id.feinn.utility.time.now
import id.feinn.utility.time.parse
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val context = FeinnLocalPlatformContext.current
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Current date: ${FeinnDate.now()}"
            )
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            Text(
                text = "Current date time: ${FeinnDateTime.now().getFormattedDateTime("dd MMMM yyyy HH:mm:ss")}"
            )
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            Text(
                text = "Parse date: ${FeinnDate.parse("2000-01-19", format = "yyyy-MM-dd")}"
            )
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            Text(
                text = "Date time parse: ${FeinnDateTime.parse("2021-01-01 12:00:00", "yyyy-MM-dd HH:mm:ss")}"
            )
            Spacer(
                modifier = Modifier.height(12.dp)
            )
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
        }
    }
}