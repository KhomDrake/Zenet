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

    testImplementation(project(":zenet:tests:robot"))
    testImplementation(libs.tests.androidx.core.testing)
    testImplementation(libs.tests.mockk.normal)
    testImplementation(libs.tests.mockk.android)
    testImplementation(libs.tests.junit)
    testImplementation(libs.tests.kotest.junit5)
    testImplementation(libs.tests.kotest.assertions)
    testImplementation(libs.tests.kotest.property)

}