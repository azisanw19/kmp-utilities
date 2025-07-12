rootProject.name = "KMPUtilities"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(
    ":composeApp",
    ":iosApp",

    ":core:utilities:time",
    ":core:utilities:launcher",
    ":core:utilities:context",
    ":core:utilities:permission",
    ":core:utilities:notification",
    ":core:utilities:byteBuffer",
    ":core:utilities:crypto",
    ":core:utilities:connectionMonitor",

    ":core:components:screenshot"
)