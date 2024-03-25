plugins {
    id("library-module")
}

android {
    namespace = "br.com.khomdrake.test.core"
}

dependencies {

    implementation(libs.threetenabp)
    implementation(libs.toolkit.livedata)
    implementation(libs.androidx.startup)
    implementation(libs.squareup.moshi.kotlin)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)

    debugApi(libs.tests.androidx.fragment.testing)

    api(libs.tests.mockk.normal) {
        exclude(module = "objenesis")
        exclude(module = "mockk-agent-jvm")
    }
    api(libs.tests.mockk.android) {
        exclude(module = "objenesis")
        exclude(module = "mockk-agent-jvm")
    }
    api(libs.tests.guava)
    api(libs.tests.objenesis)
    api(libs.tests.androidx.core.testing)
    api(libs.tests.androidx.espresso.core)
    api(libs.tests.androidx.espresso.contrib)
    api(libs.tests.androidx.espresso.intents)
    api(libs.tests.androidx.fragment.testing)
    api(libs.tests.androidx.junit)

    api(libs.tests.junit)
    api(libs.tests.compose.ui.test)
}