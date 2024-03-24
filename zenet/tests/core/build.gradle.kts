plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "br.com.khomdrake.test"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
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