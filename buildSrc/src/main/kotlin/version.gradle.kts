import util.VersionProperties

val properties = loadVersionProperties()
rootProject.extra["version"] = properties.version

tasks.register("increaseMajorVersion") {
    group = "version"
    doLast {
        properties.major += 1
        properties.store()
    }
}

tasks.register("increaseMinorVersion") {
    group = "version"
    doLast {
        properties.minor += 1
        properties.store()
    }
}

tasks.register("increasePatchVersion") {
    group = "version"
    doLast {
        properties.patch += 1
        properties.store()
    }
}

tasks.register("setReleaseVersion") {
    group = "version"
    doLast {
        properties.suffix = "SNAPSHOT"
        properties.store()
    }
}

tasks.register("setSnapshotVersion") {
    group = "version"
    doLast {
        properties.suffix = ""
        properties.store()
    }
}

tasks.register("printVersion") {
    group = "version"
    doLast {
        val version = properties.version

        println("VERSION=$version")
    }
}

fun loadVersionProperties(): VersionProperties {
    val versionProperties = VersionProperties("$rootDir/version.properties")
    versionProperties.load()

    return versionProperties
}
