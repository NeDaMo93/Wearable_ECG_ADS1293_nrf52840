plugins {
    // https://github.com/NordicSemiconductor/Android-Gradle-Plugins/blob/main/plugins/src/main/kotlin/AndroidFeatureConventionPlugin.kt
    alias(libs.plugins.nordic.feature)
    // https://developer.android.com/kotlin/parcelize
    id("kotlin-parcelize")
}

android {
    namespace = "no.nordicsemi.android.blinky.control"
}



dependencies {
    implementation(project(":blinky:spec"))

    implementation(libs.nordic.theme)
    implementation(libs.nordic.uilogger)
    implementation(libs.nordic.uiscanner)
    implementation(libs.nordic.navigation)
    implementation(libs.nordic.permission)
    implementation(libs.nordic.log.timber)

    implementation(libs.androidx.compose.material.iconsExtended)
	implementation("androidx.appcompat:appcompat:1.1.0")
	implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.pusher:pusher-java-client:1.4.0")
    implementation ("com.google.code.gson:gson:2.4")

}