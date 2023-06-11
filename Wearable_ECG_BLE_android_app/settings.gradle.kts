pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    versionCatalogs {
        create("libs") {
            from("no.nordicsemi.android.gradle:version-catalog:1.3.3")
        }
    }
}
rootProject.name = "nRF Blinky"

include(":app")
include(":scanner")
include(":blinky:spec")
include(":blinky:ui")
include(":blinky:ble")

//if (file("../Android-Common-Libraries").exists()) {
//    includeBuild("../Android-Common-Libraries")
//}
//if (file("../Android-BLE-Library").exists()) {
//    includeBuild("../Android-BLE-Library")
//}
