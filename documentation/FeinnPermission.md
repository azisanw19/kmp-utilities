![badge][badge-android]
![badge][badge-ios]
[![badge][badge-feinn-permission]]([feinn-permission-link])

# Utilities Permission

First add the dependency to your project:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("id.feinn.azisanw19:feinn-permission:$kmp_utils_version")
}
```

## Usage

```kotlin
val permissionCamera = rememberFeinnPermissionState(
    permission = FeinnPermissionType.Camera,
    onPermissionResult = {
        println("Permission result: $it")
    }
)

Button(
    onClick = {
        when (permissionCamera.status) {
            is FeinnPermissionStatus.Granted -> {
                println("permissionCamera.status: ${permissionCamera.status}")
            }
            is FeinnPermissionStatus.Denied -> {
                if (permissionCamera.status.shouldShowRationale) {
                    permissionCamera.launchSettingRequest()
                } else {
                    permissionCamera.launchPermissionRequest()
                }
            }
        }
    }
) {
    Text(
        text = "Camera Permission"
    )
}
```

## Supported Permission

### Camera

**Android**

On Android you need to add the following permission to your `AndroidManifest.xml` file:

```xml
<uses-permission android:name="android.permission.CAMERA" />
```

**iOS**

On iOS you need to add the following key to your `Info.plist` file:

```
<key>NSCameraUsageDescription</key>
<string>Camera permission is required to take pictures</string>
```

The string value is the message that will be displayed to the user when the permission is requested.

[badge-android]: http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat
[badge-ios]: http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat
[badge-feinn-permission]: https://img.shields.io/maven-central/v/id.feinn.azisanw19/feinn-permission.svg?style=flat
[feinn-permission-link]: https://central.sonatype.com/artifact/id.feinn.azisanw19/feinn-permission