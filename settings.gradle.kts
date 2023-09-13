pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/boschglobal/kuksa-android-sdk")
            credentials {
                username = System.getenv("GPR_USER")
                password = System.getenv("GPR_TOKEN")
            }
        }
        google()
        mavenCentral()
    }
}

include(":app")
include(":kuksa-sdk")
include(":samples")
