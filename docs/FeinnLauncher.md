![badge][badge-android]
![badge][badge-ios]
[![badge][badge-feinn-launcher]](https://central.sonatype.com/artifact/id.feinn.azisanw19/feinn-launcher)

# Document uri Launcher Utilities

First add the dependency to your project:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("id.feinn.azisanw19:feinn-launcher:$kmp_utils_version")
}
```

Make sure to replace $kmp_utils_version with the appropriate version of the library.

## Usage

```kotlin

val launcher = rememberFeinnLauncer()

launcher.launch("https://www.google.com")

```

[badge-feinn-launcher]: https://img.shields.io/maven-central/v/id.feinn.azisanw19/feinn-launcher.svg?style=flat
[badge-android]: http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat
[badge-ios]: http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat