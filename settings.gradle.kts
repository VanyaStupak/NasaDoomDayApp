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
        maven { setUrl("https://jitpack.io")}
    }
}

rootProject.name = "Stupak Ivan"
include(":app")
include(":data:database")
include(":data:network")
include(":data:repository")
include(":domain")
include(":core:ui")
include(":feature:asteroids")
include(":feature:host")
include(":core:platform")
include(":feature:favourites")
include(":data:source")
include(":feature:details")
include(":core:navigation")
include(":data:local")
include(":data:paging")
include(":feature:comparison")
