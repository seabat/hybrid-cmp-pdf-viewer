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

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
