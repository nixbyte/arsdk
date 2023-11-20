pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        flatDir { //Add this code
            dirs("src/main/libs")
        }
    }
}

rootProject.name = "arsdk"
include(":app")
include(":app:sdk")
