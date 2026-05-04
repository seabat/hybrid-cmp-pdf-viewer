## 仕様

### アーキテクチャ

<div align="center">
<img src="docs/architecture.png" width="200" alt="スクリーンショット1">
</div>

* 本プロジェクトでは Compose Multiplatform フレームワーク利用して Android アプリと iOS アプリを生成する。
* ルートディレクトリは Compose Multiplatform のデフォルトプロジェクトの構造である。
    * composeApp/  Android アプリ
    * ios/ Xcode プロジェクト

* Android アプリの構造
    * docs/architecture.png に示す Android アプリの構造

* iOS アプリの構造
  * docs/architecture.png に示す iOS アプリの構造
  * Android のコンポーザブル関数を ComposeUIViewController でラップし、UIViewControllerRepresentable 経由で表示する

### 画面

#### 画面遷移

* Android
    * Activity の NavHost 内で画面遷移させる
* iOS
    * NavigationStack 内で画面遷移させる
    * 画面スタックは @State private var path = [Destination]() で管理。(enum Destination: Hashable)

#### トップページ

* タイトル
    PDF ビューア
* コンテンツ
    * アプリのプライベート領域ストレージに配置したファイル一覧。しかし、暫定的に開発段階では固定のサンプルデータを表示する。
    * 各ファイルのファイル名、作成日時、サイズを表示。
    * リストのアイテムをタップするとビューアページに画面遷移する 
* コンポーネント
    * Android: Composable 関数 の TopScreen 
    * iOS: View プロトコルを実装した TopScreen

#### ビューアページ

* タイトル
    ファイル名 
* コンテンツ
    暫定的に "Hello World!" を表示。
* コンポーネント
    * Android: Composable 関数 の ViewerScreen
    * iOS: View プロトコルを実装した ViewerScreen