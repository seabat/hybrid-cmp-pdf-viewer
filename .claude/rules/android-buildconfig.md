# Android BuildConfig ルール

Android Library モジュール（`shared-data` 等）で `BuildConfig` を使う際の手順。

---

## BuildConfig を有効化する

`build.gradle.kts` の `android` ブロックに以下を追加する。

```kotlin
android {
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField("String", "FIELD_NAME", "\"value\"")
    }
}
```

> **注意**: Android Library モジュールでは `defaultConfig { versionName = "..." }` は使用不可。  
> 代わりに `buildConfigField` で `String` フィールドとして定義する。

---

## アクセス方法

`BuildConfig` のパッケージは各モジュールの `namespace` に合わせる。

```kotlin
// shared-data の場合
import dev.seabat.cmp.pdfviewer.shareddata.BuildConfig

val value = BuildConfig.FIELD_NAME
```

参照: `shared-data/build.gradle.kts`（namespace: `dev.seabat.cmp.pdfviewer.shareddata`）
