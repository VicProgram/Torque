plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose) // Asegúrate de que este plugin se aplique y esté actualizado
}

android {
    namespace = "com.example.torque"
    compileSdk = 35 // Mantén 35, está bien

    defaultConfig {
        applicationId = "com.example.torque"
        minSdk = 28
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        // MUY IMPORTANTE: La versión del compilador Compose debe coincidir con la de Kotlin.
        // Si usas Kotlin 1.9.0, necesitas Compose Compiler 1.5.1
        // Si usas Kotlin 1.9.20, necesitas Compose Compiler 1.5.4 (o superior)
        // Para Kotlin 1.9.x, 1.5.10 o 1.5.11 es común.
        // Consulta: https://developer.android.com/jetpack/androidx/releases/compose-kotlin
        kotlinCompilerExtensionVersion = "1.5.11" // Ajusta según tu versión de Kotlin
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Importa la BOM de Compose PRIMERO para gestionar las versiones
    // Revisa la última versión aquí: https://developer.android.com/jetpack/compose/bom/bom-mapping
    // La versión 2025.05.01 es la más reciente ahora mismo (mayo 2025)
    implementation(platform("androidx.compose:compose-bom:2025.05.01"))
    implementation ("androidx.compose.material3:material3:1.1.0")

    implementation("androidx.compose.material3:material3")


    // Dependencias de Compose (sin versiones, gestionadas por la BOM)
    implementation(libs.androidx.activity.compose) // Esto ya usa la versión de la BOM
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3) // Esto ya usa la versión de la BOM

    // Dependencias generales de AndroidX y Kotlin (actualizadas a versiones recientes)
    implementation(libs.androidx.core.ktx) // O "androidx.core:core-ktx:1.13.1"
    implementation(libs.androidx.lifecycle.runtime.ktx) // O "androidx.lifecycle:lifecycle-runtime-ktx:2.8.0"

    // AppCompatActivity y AppCompatDelegate
    implementation("androidx.appcompat:appcompat:1.6.1") // O la más reciente estable

    // Coil (para carga de imágenes)
    implementation("io.coil-kt:coil-compose:2.6.0") // Versión más reciente

    // Navigation Compose (si lo estás usando)
    implementation("androidx.navigation:navigation-compose:2.7.0") // Versión más reciente

    // Dependencias de test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.05.01")) // BOM también en tests
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}