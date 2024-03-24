plugins {
    id("library-module")
}

android {
    namespace = "br.com.khomdrake.tests.koin"
}

dependencies {

    implementation(libs.koin.android)
    implementation(libs.tests.androidx.junit)

}