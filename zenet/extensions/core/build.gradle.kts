plugins {
    id("library-module")
}

android {
    namespace = "br.com.khomdrake.extensions"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)

}