plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.test.bg2kiosk"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.test.bg2kiosk"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
    implementation(libs.datastore.preferences.rxjava3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.viewpager2:viewpager2:1.1.0-beta01")
    implementation("androidx.work:work-runtime:2.7.0")
    implementation("androidx.room:room-runtime:2.6.0") // Room 라이브러리
    annotationProcessor("androidx.room:room-compiler:2.6.0") // Room 컴파일러 (Java용)
    //implementation("org.apache.poi","poi","4.1.2")
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.core:core-ktx:1.6.0")
    //implementation("android.arch.lifecycle:viewmodel:1.1.1")
    //implementation("android.arch.lifecycle:extensions:1.1.1")
    //implementation("org.apache.commons:commons-io:2.7.0")
}