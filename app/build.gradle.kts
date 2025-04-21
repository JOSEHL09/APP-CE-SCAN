plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.contraentrega.ceapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.contraentrega.ceapp"
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
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += listOf("-Xenable-feature=break-continue-in-inline-lambdas")
    }
    buildFeatures {
        compose = true
        buildConfig = true // Habilitar generación de BuildConfig
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.9" // usa la versión adecuada para tu proyecto
    }

    flavorDimensions.add("environment")
    productFlavors {
        create("tst") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL_ORDERS", "\"https://cwjgrx7wl2.execute-api.us-east-1.amazonaws.com/tst/\"")
            buildConfigField("String", "BASE_URL_AUTH", "\"https://main-prd.contraentregaintranet.com/\"")
            applicationIdSuffix = ".tst"
            versionNameSuffix = "-TST"
        }
        create("prd") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL_ORDERS", "\"https://cwjgrx7wl2.execute-api.us-east-1.amazonaws.com/prd/\"")
            buildConfigField("String", "BASE_URL_AUTH", "\"https://main-prd.contraentregaintranet.com/\"")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    //implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Añade estas si no están en libs.versions.toml
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.compose.material3:material3:1.1.2")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //jose añadio esto el gozu
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    implementation("androidx.camera:camera-core:1.3.0")
    implementation("androidx.camera:camera-camera2:1.3.0")
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")
    implementation("androidx.camera:camera-extensions:1.3.0")

    implementation("com.google.mlkit:barcode-scanning:17.2.0")
    implementation ("androidx.camera:camera-camera2:1.1.0")
    implementation ("androidx.camera:camera-lifecycle:1.1.0")
    implementation ("androidx.camera:camera-view:1.0.0-alpha32")

    implementation("com.google.accompanist:accompanist-permissions:0.31.5-beta")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}