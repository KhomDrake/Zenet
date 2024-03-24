plugins {
    id("library-module")
}

android {
    namespace = "br.com.khomdrake.extensions.paging"
}

dependencies {

    implementation(project(":zenet:extensions:core"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    implementation(libs.androidx.paging)
}