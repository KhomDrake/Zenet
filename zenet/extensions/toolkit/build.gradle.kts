plugins {
    id("library-module")
}

android {
    namespace = "br.com.khomdrake.extensions.toolkit"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    implementation(libs.toolkit.statemachine)
}