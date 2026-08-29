# hybrid-cmp-pdf-viewer

KMP + CMP のハイブリッド構成の PDF ビューアアプリ。
iOS は SwiftUI ナビゲーション + ComposeUIViewController、Android は Jetpack Navigation + Compose。

## Tech Stack

- Kotlin Multiplatform / Compose Multiplatform 1.10.3
- Android: Jetpack Navigation Compose
- iOS: SwiftUI NavigationStack + ComposeUIViewController
- DI: Koin

## Project Structure

Gradle マルチモジュール構成。KMP 公式推奨では単一 `shared` モジュールだが、本プロジェクトは
クリーンアーキテクチャ（UI / Domain / Data）に沿って `shared` を 3 モジュールに分割している。
iOS アプリ（`iosApp`）は Xcode 管理で Gradle の対象外。

| モジュール | 役割 / 層 | ターゲット |
|---|---|---|
| `androidApp` | Android アプリ（Jetpack Navigation Compose） | Android |
| `shared-ui` | UI 層（Compose Multiplatform） | Android + iOS |
| `shared-domain` | ドメイン層（UseCase / Entity / Repository IF） | Android + iOS |
| `shared-data` | データ層（Repository 実装 / DataSource） | Android + iOS |
| `iosApp` | iOS アプリ（SwiftUI・Xcode 管理、Gradle 非対象） | iOS |

各 shared モジュールは AGP の `androidLibrary { }` プラグイン + `iosArm64()` / `iosSimulatorArm64()` を
ターゲットに持ち、`commonMain` / `androidMain` / `iosMain`（+ `commonTest`）のソースセットで構成される。
全 Kotlin パッケージのルートは `dev.seabat.cmp.pdfviewer`（ツリー内では `.../` で省略）。

```
hybrid-cmp-pdf-viewer/
├── androidApp/                     # Android アプリ (namespace: ...pdfviewer)
│   └── src/main/kotlin/.../pdfviewer/
│       └── screen/{top,information}/
├── shared-ui/                      # UI 層 (namespace: ...sharedui)
│   └── src/
│       ├── commonMain/kotlin/.../pdfviewer/
│       │   ├── screen/{top,information,viewer}/
│       │   ├── navigation/  theme/  di/
│       │   └── App.kt, Platform.kt
│       ├── androidMain/.../screen/viewer/, resource/
│       └── iosMain/.../viewcontroller/{top,information,viewer}/, screen/viewer/
├── shared-domain/                  # ドメイン層 (namespace: ...shareddomain)
│   └── src/{commonMain,commonTest,androidMain,iosMain}/kotlin/.../shareddomain/
│       └── usecase/  repository/  entity/  util/  di/
├── shared-data/                    # データ層 (namespace: ...shareddata)
│   └── src/{commonMain,commonTest,androidMain,iosMain}/kotlin/.../shareddata/
│       └── datasource/  repository/  di/    # datasource は android/ios に actual 実装
└── iosApp/                         # iOS アプリ (SwiftUI, Xcode 管理)
    └── iosApp/
        ├── iOSApp.swift, NavigationView.swift
        └── Screen/{Top,Information,Viewer}/
```

参考: [Compose Multiplatform / KMP 推奨プロジェクト構成](https://kotlinlang.org/docs/multiplatform/multiplatform-project-recommended-structure.html)

## Rules

@.claude/rules/screen-addition.md
@.claude/rules/usecase-addition.md
@.claude/rules/android-buildconfig.md
@.claude/rules/dialog-addition.md
