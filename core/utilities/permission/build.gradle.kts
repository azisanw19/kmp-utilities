import com.vanniktech.maven.publish.SonatypeHost
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.vanniktechMavenPublish)
}

kotlin {
    explicitApi()
    jvmToolchain(libs.versions.jvm.get().toInt())

    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.get().compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    androidTarget()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FeinnPermission"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core)
            implementation(libs.androidx.activity)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
    }
}

android {
    namespace = "id.feinn.utility.permission"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig.minSdk = libs.versions.android.minSdk.get().toInt()
    buildFeatures.compose = true
}

val mavenProperties = Properties()
val mavenPropertiesFile = rootProject.file("maven.info.properties")

if (mavenPropertiesFile.exists()) {
    mavenProperties.load(FileInputStream(mavenPropertiesFile))
}

mavenPublishing {
    coordinates(
        groupId = mavenProperties.getProperty("groupId"),
        artifactId = mavenProperties.getProperty("artifactIdPermission"),
        version = mavenProperties.getProperty("version")
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("KMP Utilities File")
        description.set("FeinnPermission is a custom library that handle permission for Android and iOS applications")
        inceptionYear.set("2025")
        url.set("https://github.com/azisanw19/kmp-utilities")

        licenses {
            license {
                name.set("Apache License 2.0")
                url.set("https://github.com/azisanw19/kmp-utilities/blob/main/LICENSE")
            }
        }

        // Specify developers information
        developers {
            developer {
                id.set(mavenProperties.getProperty("developerId"))
                name.set(mavenProperties.getProperty("developerName"))
                email.set(mavenProperties.getProperty("developerEmail"))

            }
        }

        // Specify SCM information
        scm {
            url.set("https://github.com/azisanw19/kmp-utilities")
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()
}

task("testClasses") {}