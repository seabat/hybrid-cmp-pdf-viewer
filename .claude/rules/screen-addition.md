# 画面追加ルール

画面構成には **方法A**・**方法B**・**方法C** の3種類がある。
参考図: `docs/CMP_In_Native化案-UI共通化.png`

---

## 方法A：Scaffold を androidMain に置く

**採用例:** TopScreen

iOS 側で Header と Content を個別の ComposeView として SwiftUI の VStack に配置する。
SafeArea の細かい制御が必要な画面に向いている。

### 作成するファイル

**commonMain** — Header と Content のみ（Scaffold なし）

```
screen/{screenName}/
├── {ScreenName}Content.kt    # コンテンツ（TopContent.kt を参照）
└── {ScreenName}Header.kt     # TopAppBar（TopHeader.kt を参照）
```

**androidMain** — Scaffold を持つ Screen

```
screen/{screenName}/
└── {ScreenName}Screen.kt     # Scaffold + Header + Content（TopScreen.kt を参照）
```

**iosMain** — Header・Content それぞれに ViewController

```
viewcontroller/{screenName}/
├── {ScreenName}HeaderViewController.kt   # TopHeaderViewController.kt を参照
└── {ScreenName}ContentViewController.kt  # TopContentViewController.kt を参照
```

**Swift** — VStack で Header と Content を個別配置

```
iosApp/Screen/
└── {ScreenName}Screen.swift    # TopScreen.swift を参照
```

- SwiftUI の `VStack(spacing: 0)` で Header + Content を並べる
- Header は `.frame(height: 64)` + `.background(...ignoresSafeArea(edges: .top))`
- Content は `.background(...ignoresSafeArea(edges: .bottom))`
- `.navigationBarHidden(true)` は不要（SwiftUI が SafeArea を管理）

---

## 方法B：Scaffold を commonMain に置く

**採用例:** InformationScreen

Scaffold ごと CMP コンポーネントとして共有する。iOS は Scaffold 全体を1つの ComposeView でラップする。

### 作成するファイル

**commonMain** — Header・Content・Scaffold をすべて共有

```
screen/{screenName}/
├── {ScreenName}Content.kt    # コンテンツ（ViewerContent.kt を参照）
├── {ScreenName}Header.kt     # TopAppBar + 戻るボタン（ViewerHeader.kt を参照）
└── {ScreenName}Scaffold.kt   # Scaffold で Header + Content をラップ（ViewerScaffold.kt を参照）
```

- **Header**: `onNavigateBack: () -> Unit` を受け取り、戻るボタンを `navigationIcon` に配置。
- **Scaffold**: `containerColor = AppColors.contentContainer.toComposeColor()` を設定。

**androidMain** — Scaffold を呼ぶだけの薄いラッパー

```
screen/{screenName}/
└── {ScreenName}Screen.kt     # ViewerScreen.kt を参照
```

**iosMain** — Scaffold 全体を1つの ViewController でラップ

```
viewcontroller/{screenName}/
└── {ScreenName}ScaffoldViewController.kt   # ViewerScaffoldViewController.kt を参照
```

**Swift** — Scaffold 全体を1つの ComposeView でラップ

```
iosApp/Screen/
└── {ScreenName}Screen.swift    # ViewerScreen.swift を参照
```

- `@Environment(\.dismiss)` で戻る処理を実装。
- `.ignoresSafeArea()` と `.navigationBarHidden(true)` を付ける。

---

## 方法C：Scaffold を commonMain に置き、Scaffold を画面として直接使用する

**採用例:** ViewerScreen

Android は NavHost から Scaffold を直接呼び出し、iOS は ScaffoldComposeView を NavigationView の `navigationDestination` で直接使用する。Screen コンポーザブル / Screen struct を作成しないため、ボイラープレートが最小になる。

### 作成するファイル

**commonMain** — 方法B と同じく Header・Content・Scaffold をすべて共有

```
screen/{screenName}/
├── {ScreenName}Content.kt    # コンテンツ（ViewerContent.kt を参照）
├── {ScreenName}Header.kt     # TopAppBar + 戻るボタン（ViewerHeader.kt を参照）
└── {ScreenName}Scaffold.kt   # Scaffold で Header + Content をラップ（ViewerScaffold.kt を参照）
```

**androidMain** — Screen ファイルは作成しない（NavHost から Scaffold を直接呼び出す）

**iosMain** — 方法B と同じく Scaffold 全体を1つの ViewController でラップ

```
viewcontroller/{screenName}/
└── {ScreenName}ScaffoldViewController.kt   # ViewerScaffoldViewController.kt を参照
```

**Swift** — Screen struct は作成せず、ScaffoldComposeView のみ定義する

```
iosApp/Screen/
└── {ScreenName}Screen.swift    # ScaffoldComposeView のみ（ViewerScreen.swift を参照）
```

- `@Environment(\.dismiss)` は使用しない。NavigationView.swift 側で `path.removeLast()` を渡す。
- `.ignoresSafeArea()` と `.navigationBarHidden(true)` は NavigationView.swift の `navigationDestination` 側で付ける。

---

## ナビゲーションの更新

### 方法A・B の場合

**Android: NavigationApp.kt**

```kotlin
// ルートを追加
composable("{screenName}") {
    {ScreenName}Screen(onNavigateBack = { navController.popBackStack() })
}

// 遷移元に onNavigateTo{ScreenName} を渡す
onNavigateTo{ScreenName} = { navController.navigate("{screenName}") }
```

**iOS: NavigationView.swift**

```swift
// Destination enum にケースを追加
enum Destination: Hashable {
    case {screenName}
}

// navigationDestination に分岐を追加
case .{screenName}:
    {ScreenName}Screen()
```

### 方法C の場合

**Android: NavigationApp.kt**

```kotlin
// Screen を介さず Scaffold を直接呼び出す
composable("{screenName}/{param}") { backStackEntry ->
    val param = backStackEntry.arguments?.getString("param") ?: ""
    {ScreenName}Scaffold(
        param = param,
        onNavigateBack = { navController.popBackStack() }
    )
}

// 遷移元に onNavigateTo{ScreenName} を渡す
onNavigateTo{ScreenName} = { param -> navController.navigate("{screenName}/$param") }
```

**iOS: NavigationView.swift**

```swift
// Destination enum にケースを追加（パラメータ付きの場合）
enum Destination: Hashable {
    case {screenName}(param: String)
}

// navigationDestination で ScaffoldComposeView を直接配置
// onNavigateBack は path.removeLast() を渡す（dismiss() は使わない）
case .{screenName}(let param):
    {ScreenName}ScaffoldComposeView(param: param, onNavigateBack: { path.removeLast() })
        .ignoresSafeArea()
        .navigationBarHidden(true)
```

---

## 遷移元ヘッダーの更新

TopHeader など遷移元の Header に遷移トリガーを追加する場合：

**Kotlin (commonMain):**
- `onNavigateTo{ScreenName}: () -> Unit` パラメータを追加
- `TopAppBar` の `actions` に `IconButton` を配置

**iosMain の ViewController:**
- 同パラメータを追加して Composable へ渡す

**Swift の ComposeView ラッパー:**
- `onNavigateTo{ScreenName}: () -> Void` プロパティを追加
- `makeUIViewController` 内でコールバックを渡す

**Swift の画面（方法A）:**
- コールバック内で `path.append(.{screenName})` を呼ぶ

---

## Compose Navigation Graph アノテーション

画面を追加したら、**skydoves NavGraph** のアノテーションを付与する。
ルート定義は `shared-ui/src/commonMain/.../navigation/Screen.kt` の `sealed interface Screen` に追加する。

### アノテーション一覧

| アノテーション | 付与対象 | 説明 |
|---|---|---|
| `@NavGraphRoot` | ルート画面のみ（TopScreen） | ナビゲーショングラフの起点を示す |
| `@NavDestination(route = Screen.Xxx::class)` | すべての画面 | ナビゲーション先として登録する |
| `@NavEdge(to = Screen.Xxx::class, label = "ラベル")` | 遷移元の画面 | 画面間のエッジを定義する |
| `@NavPreview(route = Screen.Xxx::class, primary = true)` | プレビュー関数 | NavGraph Previews パネルにサムネイルを表示する |

### 方法別の付与場所

#### 方法A・B（androidMain に Screen.kt がある場合）

`{ScreenName}Screen.kt`（androidMain）の Screen コンポーザブルに付与する。

```kotlin
// androidApp/src/main/kotlin/.../screen/{screenName}/{ScreenName}Screen.kt

@NavGraphRoot               // ルート画面のみ
@NavDestination(route = Screen.{ScreenName}::class)
@NavEdge(to = Screen.{NextScreen}::class, label = "to{NextScreen}")  // 遷移先がある場合
@Composable
fun {ScreenName}Screen(...) { ... }

@NavPreview(route = Screen.{ScreenName}::class, primary = true)
@Preview
@Composable
fun {ScreenName}ScreenPreview() {
    {ScreenName}Screen(...)
}
```

#### 方法C（commonMain に Scaffold.kt のみの場合）

androidMain に Screen ファイルが存在しないため、`{ScreenName}Scaffold.kt`（commonMain）に付与する。

```kotlin
// shared-ui/src/commonMain/.../screen/{screenName}/{ScreenName}Scaffold.kt

@NavDestination(route = Screen.{ScreenName}::class)
@Composable
fun {ScreenName}Scaffold(...) { ... }

@NavPreview(route = Screen.{ScreenName}::class, primary = true)
@Preview
@Composable
fun {ScreenName}ScaffoldPreview() {
    {ScreenName}Scaffold(...)
}
```

### `@Preview` の import

commonMain・androidMain いずれも以下を使用する（`org.jetbrains.compose` は deprecated）。

```kotlin
import androidx.compose.ui.tooling.preview.Preview
```

---

## 命名・配置のルール

### 方法A

| レイヤー | パッケージ / ディレクトリ | クラス名のパターン |
|---------|------------------------|-----------------|
| commonMain | `screen/{screenName}/` | `{ScreenName}Content`, `{ScreenName}Header` |
| androidMain | `screen/{screenName}/` | `{ScreenName}Screen`（Scaffold を持つ） |
| iosMain | `viewcontroller/{screenName}/` | `{ScreenName}HeaderViewController`, `{ScreenName}ContentViewController` |
| Swift | `iosApp/Screen/` | `{ScreenName}Screen`, `{ScreenName}HeaderComposeView`, `{ScreenName}ContentComposeView` |

### 方法B

| レイヤー | パッケージ / ディレクトリ | クラス名のパターン |
|---------|------------------------|-----------------|
| commonMain | `screen/{screenName}/` | `{ScreenName}Content`, `{ScreenName}Header`, `{ScreenName}Scaffold` |
| androidMain | `screen/{screenName}/` | `{ScreenName}Screen`（薄いラッパー） |
| iosMain | `viewcontroller/{screenName}/` | `{ScreenName}ScaffoldViewController` |
| Swift | `iosApp/Screen/` | `{ScreenName}Screen`, `{ScreenName}ScaffoldComposeView` |

### 方法C

| レイヤー | パッケージ / ディレクトリ | クラス名のパターン |
|---------|------------------------|-----------------|
| commonMain | `screen/{screenName}/` | `{ScreenName}Content`, `{ScreenName}Header`, `{ScreenName}Scaffold` |
| androidMain | （Screenファイルなし） | — |
| iosMain | `viewcontroller/{screenName}/` | `{ScreenName}ScaffoldViewController` |
| Swift | `iosApp/Screen/` | `{ScreenName}ScaffoldComposeView`（Screen struct なし） |
