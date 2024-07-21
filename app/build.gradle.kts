import org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp") version "1.8.0-1.0.8"
    id("org.jetbrains.kotlin.kapt")
    id("org.jlleitschuh.gradle.ktlint")
}

kotlin {
    sourceSets {
        debug {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        release {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}

android {
    namespace = "com.example.tuicodewars"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tuicodewars"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.withType(type = KaptGenerateStubsTask::class) {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    //  MockK
    testImplementation("io.mockk:mockk:1.13.2")
    androidTestImplementation("io.mockk:mockk-android:1.13.2")
    //  compose destination
    implementation("io.github.raamcosta.compose-destinations:core:1.7.27-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.7.27-beta")
    //  dagger-hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.20")
    //  retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //  gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Okhttp
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.6")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.6")
    //  coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    //  lifecycle-compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.3")
    // swipe refresh
    implementation("androidx.compose.material:material:1.6.8")
}

kapt {
    correctErrorTypes = true
}

ktlint {
    android.set(true)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(false)
    enableExperimentalRules.set(true)
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}
