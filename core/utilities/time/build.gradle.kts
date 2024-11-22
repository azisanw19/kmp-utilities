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

val applicationProperties = Properties()
val applicationPropertiesFile = rootProject.file("maven.properties")

if (applicationPropertiesFile.exists()) {
    applicationProperties.load(FileInputStream(applicationPropertiesFile))
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
            baseName = "FeinnTime"
            isStatic = true
        }
    }
    
    sourceSets {
        
        commonMain.dependencies {
            implementation(compose.runtime)
        }
    }
}

android {
    namespace = "id.feinn.utility.time"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig.minSdk = libs.versions.android.minSdk.get().toInt()
    buildFeatures.compose = true
}

val mavenProperties = Properties()
val mavenPropertiesFile = rootProject.file("maven.properties")

if (mavenPropertiesFile.exists()) {
    mavenProperties.load(FileInputStream(mavenPropertiesFile))
}

mavenPublishing {
    coordinates(
        groupId = "id.feinn.azisanw19",
        artifactId = "feinn-date-time",
        version = "1.0.0-alpha01"
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("KMP Utilities Date Time")
        description.set("FeinnDateTime is a custom library that enables date and time management in Android and iOS applications")
        inceptionYear.set("2024")
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
                id.set("azisanw19")
                name.set("Aziz Anwar")
                email.set("azisanw19@gmail.com")
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