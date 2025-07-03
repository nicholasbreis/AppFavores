plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // ✅ ESSENCIAL para inicializar o Firebase!
}

android {
    namespace = "com.example.appfavores"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.appfavores"
        minSdk = 26
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.android.material:material:1.6.0")
    implementation("com.google.android.material:material:1.11.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.15.0"))
    implementation(libs.firebase.firestore)
    implementation ("com.google.firebase:firebase-auth:22.3.1")

    // Testes
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
