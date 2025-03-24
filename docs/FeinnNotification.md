![badge][badge-android]
![badge][badge-ios]
[![badge][badge-feinn-permission]](https://central.sonatype.com/artifact/id.feinn.azisanw19/feinn-notification)

# Utilities Notification

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
```

## Notification Permission

### Notification

**Android**

On Android API version 33 and up, you need to add the following permission to your `AndroidManifest.xml` file:

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
```

**iOS**

On iOS you need to add the following key to your `Info.plist` file:

```
<key>NSUserNotificationsUsageDescription</key>
<string>Notifications permission is required to show notifications</string>
```

[badge-android]: http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat
[badge-ios]: http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat
[badge-feinn-notification]: https://img.shields.io/maven-central/v/id.feinn.azisanw19/feinn-notification.svg?style=flat
