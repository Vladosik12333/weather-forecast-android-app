import java.util.Properties

val secretsPropertiesFile = rootProject.file("secrets.properties")
val secrets = Properties()

if (secretsPropertiesFile.exists()) {
    secrets.load(secretsPropertiesFile.inputStream())
} else {
    throw GradleException("secrets.properties not found! Rolling down.")
}

val forecastApiKey: String = secrets.getProperty("FORECAST_API_KEY") ?: ""
val aiModelApiKey: String = secrets.getProperty("AI_MODEL_API_KEY") ?: ""

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "by.vb.weather_forecast_project"
    compileSdk = 35

    defaultConfig {
        applicationId = "by.vb.weather_forecast_project"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "FORECAST_API_KEY", "\"$forecastApiKey\"")
        buildConfigField("String", "AI_MODEL_API_KEY", "\"$aiModelApiKey\"")
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

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    testImplementation(libs.espresso.core)
    testImplementation(libs.ext.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.core.testing)
    testImplementation(libs.espresso.intents)
    testImplementation(libs.robolectric.v413)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.rules)
    androidTestImplementation(libs.espresso.intents)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.junit.ktx)

    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.glide)
    implementation(libs.openai.java)
}