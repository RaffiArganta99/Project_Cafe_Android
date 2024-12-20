plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.Bonanza"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.Bonanza"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    viewBinding {
        var enabled = true
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation ("com.github.qamarelsafadi:CurvedBottomNavigation:0.1.3")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("androidx.core:core:1.10.0") // pastikan versinya sesuai dengan yang terbaru
    implementation ("androidx.appcompat:appcompat:1.6.1") // pastikan versinya sesuai
    implementation("com.github.bumptech.glide:glide:4.15.1") // Versi dapat disesuaikan
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    // Navigation Component



}
