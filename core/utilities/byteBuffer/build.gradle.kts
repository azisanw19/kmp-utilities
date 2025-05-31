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

    // JVM & Android
    jvm()
    androidTarget()

    // JavaScript (both browser and Node.js)
    js(IR) {
        browser()
        nodejs()
    }

    // iOS targets
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // macOS targets
    macosX64()
    macosArm64()

    // Linux targets
    linuxX64()
    linuxArm64()

    // Windows MinGW x64
    mingwX64()

    // watchOS targets
    watchosX64()
    watchosArm64()
    watchosSimulatorArm64()

    // tvOS targets
    tvosArm64()
    tvosSimulatorArm64()

    // Configure framework output for Apple targets
    listOf(
        iosX64(), iosArm64(), iosSimulatorArm64(),
        macosX64(), macosArm64(),
        watchosX64(), watchosArm64(), watchosSimulatorArm64(),
        tvosArm64(), tvosSimulatorArm64()
    ).forEach { appleTarget ->
        appleTarget.binaries.framework {
            baseName = "FeinnByteBuffer"
            isStatic = true
        }
    }

    sourceSets {

        commonMain.dependencies {
            implementation(compose.runtime)
        }

        commonTest.dependencies {
            implementation(kotlin("test")) // kotlin.test for common testing
        }
    }
}

android {
    namespace = "id.feinn.utility.bytebuffer"
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
        artifactId = mavenProperties.getProperty("artifactIdByteBuffer"),
        version = mavenProperties.getProperty("version")
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("KMP Utilities Byte Buffer")
        description.set("FeinnByteBuffer is a library to handle byte buffer")
        inceptionYear.set(mavenProperties.getProperty("year"))
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