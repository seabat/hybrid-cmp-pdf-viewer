plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.skydoves.navgraph)
}

android {
    namespace = "dev.seabat.cmp.pdfviewer"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "dev.seabat.cmp.pdfviewer"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // [ワークアラウンド] shared-ui/build.gradle.kts の assembleAndroidMainComposeResources タスクと対になる設定
    // shared-ui の CMP リソースを Android assets に含める（shared-ui/build.gradle.kts のコメント参照）
    sourceSets {
        getByName("main") {
            assets.srcDirs(
                rootProject.file("shared-ui/build/generated/compose/resourceGenerator/assembledResources/androidMain")
            )
        }
    }
}

// [ワークアラウンド] shared-ui:assembleAndroidMainComposeResources → mergeDebugAssets / mergeReleaseAssets
// の順序を明示的に宣言する（Gradle の implicit dependency 検証を満たすため）
evaluationDependsOn(":shared-ui")
afterEvaluate {
    val sharedUiAssembleTask = project(":shared-ui").tasks.findByName("assembleAndroidMainComposeResources")
    if (sharedUiAssembleTask != null) {
        listOf("mergeDebugAssets", "mergeReleaseAssets").forEach { taskName ->
            tasks.findByName(taskName)?.dependsOn(sharedUiAssembleTask)
        }
    }
}


dependencies {
    implementation(project(":shared-ui"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.navigation.compose)
    implementation(compose.material3)
    implementation(compose.components.resources)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    debugImplementation(libs.compose.uiTooling)
}
