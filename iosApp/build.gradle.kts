tasks.register<Delete>("clean") {
    group = "Project Setup"
    description = "Delete Xcode project created with xcodegen"
    delete(
        "$projectDir/ExampleKMPUtilities.xcodeproj/",
        "$projectDir/Info.plist",
    )
}

tasks.register<Exec>("generateXcodeProject") {
    group = "Project Setup"
    description = "Use xcodegen to create an Xcode project"
    dependsOn(tasks.getByPath(":composeApp:linkDebugFrameworkIosArm64"))
    val paths = listOf(
        "/usr/local/bin/xcodegen",
        "/opt/homebrew/bin/xcodegen",
    )
    val xcodegen = paths.firstOrNull { file(it).exists() }
        ?: error("xcodegen not found at $paths")
    commandLine(xcodegen)
}

tasks.register<Exec>("openXcode") {
    group = "Project Setup"
    description = "Open Xcode project"
    dependsOn("generateXcodeProject")
    commandLine("open", "ExampleKMPUtilities.xcodeproj")
}
