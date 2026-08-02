import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.skydoves.navgraph)
    id("co.touchlab.skie") version "0.10.12"
}

kotlin {
    androidLibrary {
        namespace = "dev.seabat.cmp.pdfviewer.sharedui"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "SharedUI"
            isStatic = true
            export(project(":shared-domain"))
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            api(project(":shared-domain"))
            implementation(project(":shared-data"))
            implementation(libs.compose.uiToolingPreview)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.androidx.compose)
            implementation(libs.koin.test)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "dev.seabat.cmp.pdfviewer.sharedui.generated.resources"
}

// [ワークアラウンド] com.android.kotlin.multiplatform.library + CMP 互換性問題
// CMP の CopyResourcesToAndroidAssetsTask が com.android.library 向けの API を前提としており、
// com.android.kotlin.multiplatform.library プラグインが outputDirectory を設定しないため、
// copyAndroidMainComposeResourcesToAndroidAssets タスクが機能せず Android 向けリソースが AAR に含まれない。
//
// 解除条件: 以下のいずれかで copyAndroidMainComposeResourcesToAndroidAssets が正常動作したら削除する
//   - CMP が com.android.kotlin.multiplatform.library に対応した場合
//   - AGP が新しいプラグインで assets ディレクトリの API を整備した場合
//   確認方法: `./gradlew :shared-ui:copyAndroidMainComposeResourcesToAndroidAssets` でエラーが出なければ解除可
val assembleAndroidMainComposeResources by tasks.registering(Copy::class) {
    val preparedDir = layout.buildDirectory
        .dir("generated/compose/resourceGenerator/preparedResources/commonMain/composeResources")
    val outputDir = layout.buildDirectory
        .dir("generated/compose/resourceGenerator/assembledResources/androidMain/composeResources/dev.seabat.cmp.pdfviewer.sharedui.generated.resources")
    dependsOn("prepareComposeResourcesTaskForCommonMain")
    from(preparedDir)
    into(outputDir)
}

afterEvaluate {
    tasks.findByName("bundleAndroidMainAar")?.dependsOn(assembleAndroidMainComposeResources)
    tasks.findByName("androidPreBuild")?.dependsOn(assembleAndroidMainComposeResources)
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
