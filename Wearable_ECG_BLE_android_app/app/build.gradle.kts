plugins {
    // https://github.com/NordicSemiconductor/Android-Gradle-Plugins/blob/main/plugins/src/main/kotlin/AndroidApplicationComposeConventionPlugin.kt
    alias(libs.plugins.nordic.application.compose)
    // https://github.com/NordicSemiconductor/Android-Gradle-Plugins/blob/main/plugins/src/main/kotlin/AndroidHiltConventionPlugin.kt
    alias(libs.plugins.nordic.hilt)
}

android {
    namespace = "no.nordicsemi.android.blinky"
    defaultConfig {
        applicationId = "no.nordicsemi.android.nrfblinky"
        resourceConfigurations.add("en")
		versionCode = 1
		versionName = "1.0"

    }
}

dependencies {
    implementation(project(":scanner"))
    implementation(project(":blinky:spec"))
    implementation(project(":blinky:ui"))
    implementation(project(":blinky:ble"))


    implementation(libs.nordic.theme)
    implementation(libs.nordic.navigation)

    implementation(libs.timber)

    implementation(libs.androidx.activity.compose)

    // Add the JTransforms dependency here
    implementation("com.github.wendykierp:JTransforms:3.1")

}