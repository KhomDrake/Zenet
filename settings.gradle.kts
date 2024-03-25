pluginManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
}

rootProject.name = "zenet"

include(":app")
include(":zenet:request")
include(":zenet:tests:core")
include(":zenet:extensions:core")
include(":zenet:extensions:paging")
include(":zenet:extensions:toolkit")
include(":zenet:tests:koin")
