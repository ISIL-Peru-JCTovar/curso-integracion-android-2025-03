plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "isil.edu.pe.integracion.sesion1.demo1"
    compileSdk = 35

    defaultConfig {
        applicationId = "isil.edu.pe.integracion.sesion1.demo1"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

apply(plugin="com.android.application")
apply(plugin="com.google.gms.google-services")

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.firebase.analytics)
    implementation("com.google.firebase:firebase-analytics:22.3.0")
    implementation("com.google.firebase:firebase-analytics-ktx:22.3.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
    implementation("com.google.firebase:firebase-firestore:25.1.2")
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.2")
    implementation("com.google.firebase:firebase-storage:21.0.1")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.1")
    implementation(libs.androidx.recyclerview.v121)
    //implementation("com.github.dhaval2404:imagepicker:2.1")
    //implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //Autenticacion de Google
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    //API de Twitter (X)
    //implementation("com.twitter.sdk.android:twitter:3.3.0")
    //implementation("org.twitter4j:twitter4j-core:4.0.7")
    //implementation("io.github.takke:jp.takke.twitter4j-v2:1.4.4")
    //implementation("io.github.tyczj:tweedle:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("com.github.scribejava:scribejava-apis:8.3.3")
    //API REST
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    //Push Notifications
    implementation("com.google.firebase:firebase-messaging:24.1.1")
    implementation("androidx.activity:activity-ktx:1.7.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}