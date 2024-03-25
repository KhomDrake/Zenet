plugins {
    id("library-module")
}

android {
    namespace = "br.com.khomdrake.request"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.datastore)
    implementation(libs.google.material)
    implementation(libs.toolkit.livedata)
    implementation(libs.timber)

}