# ダイアログ追加ルール

Android と iOS それぞれのフレームワーク標準ダイアログを表示する方法。
CMP (commonMain) の `AlertDialog` は使わない。

参考: https://kotlinlang.org/docs/multiplatform/compose-swiftui-integration.html#use-swiftui-inside-compose-multiplatform

---

## アーキテクチャ

- **Android**: `androidMain` 限定の Jetpack Compose `AlertDialog`
- **iOS**: Kotlin → Swift へコールバックを渡し、Swift 側で SwiftUI `.alert` を表示

iOS は参考 URL のアーキテクチャ（Kotlin ↔ Swift のコールバック統合）に従う。

---

## 採用例：InformationScreen（方法B 構成）

方法B（Scaffold を commonMain に置く構成）での実装例。

---

## 変更ファイルと手順

### 1. Content（commonMain）

トリガーとなる UI 要素に `onXxxTapped: () -> Unit = {}` パラメータを追加し、`clickable` などで呼び出す。

```kotlin
// {ScreenName}Content.kt
@Composable
fun {ScreenName}Content(
    modifier: Modifier = Modifier,
    onXxxTapped: () -> Unit = {}
) {
    // ...
    Text(phrase, modifier = Modifier.clickable { onXxxTapped() })
}
```

### 2. Scaffold（commonMain）

`onShowXxxAlert: () -> Unit = {}` パラメータを追加し、Content へ渡す。

```kotlin
// {ScreenName}Scaffold.kt
@Composable
fun {ScreenName}Scaffold(
    onNavigateBack: () -> Unit,
    onShowXxxAlert: () -> Unit = {}
) {
    Scaffold(...) { padding ->
        {ScreenName}Content(
            modifier = Modifier.padding(padding),
            onXxxTapped = onShowXxxAlert
        )
    }
}
```

### 3. Screen（androidMain）

`showDialog` state を管理し、`AlertDialog` を表示する。
Scaffold の `onShowXxxAlert` に `{ showDialog = true }` を渡す。

```kotlin
// {ScreenName}Screen.kt
@Composable
fun {ScreenName}Screen(onNavigateBack: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("確認") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) { Text("OK") }
            },
            text = { Text("メッセージ") }
        )
    }

    {ScreenName}Scaffold(
        onNavigateBack = onNavigateBack,
        onShowXxxAlert = { showDialog = true }
    )
}
```

### 4. ScaffoldViewController（iosMain）

`onShowAlert: () -> Unit` パラメータを追加し、Scaffold へ渡す。

```kotlin
// {ScreenName}ScaffoldViewController.kt
fun {ScreenName}ScaffoldViewController(
    onNavigateBack: () -> Unit,
    onShowAlert: () -> Unit
) = ComposeUIViewController {
    {ScreenName}Scaffold(
        onNavigateBack = onNavigateBack,
        onShowXxxAlert = onShowAlert
    )
}
```

### 5. Screen.swift（iosApp）

`InformationScaffoldComposeView` に `onShowAlert` プロパティを追加し、`makeUIViewController` で Kotlin 側へ渡す。
`InformationScreen` に `@State showAlert` を追加して SwiftUI `.alert` で表示する。

```swift
// {ScreenName}Screen.swift
private struct {ScreenName}ScaffoldComposeView: UIViewControllerRepresentable {
    var onNavigateBack: () -> Void
    var onShowAlert: () -> Void  // 追加

    func makeUIViewController(context: Context) -> UIViewController {
        {ScreenName}ScaffoldViewControllerKt.{ScreenName}ScaffoldViewController(
            onNavigateBack: { onNavigateBack() },
            onShowAlert: { onShowAlert() }  // 追加
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct {ScreenName}Screen: View {
    @Environment(\.dismiss) private var dismiss
    @State private var showAlert = false  // 追加

    var body: some View {
        {ScreenName}ScaffoldComposeView(
            onNavigateBack: { dismiss() },
            onShowAlert: { showAlert = true }  // 追加
        )
        .ignoresSafeArea()
        .navigationBarHidden(true)
        .alert("確認", isPresented: $showAlert) {  // 追加
            Button("OK") { showAlert = false }
        } message: {
            Text("メッセージ")
        }
    }
}
```

---

## 方法A 構成の場合

方法A（Scaffold を androidMain に置く構成）では、Android と iOS でダイアログの起点が異なる。

- **Android**: `{ScreenName}Screen.kt` (androidMain) で `showDialog` を管理し `AlertDialog` を表示。`{ScreenName}Content` へ `onXxxTapped` コールバックを渡す。
- **iOS**: `{ScreenName}ContentViewController.kt` (iosMain) に `onShowAlert` を追加し、Swift 側の `{ScreenName}ContentComposeView` 経由でコールバックを渡す。

---

## 命名規約

| パラメータ名 | 用途 |
|------------|------|
| `onXxxTapped` | Content 内のトリガー要素のコールバック |
| `onShowXxxAlert` | Scaffold が Content から受け取り上位へ渡すコールバック |
| `onShowAlert` | ViewController / Swift ComposeView が受け取るコールバック |
