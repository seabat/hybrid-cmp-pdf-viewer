### モジュール構成

Compose Multiplatform フレームワークの構成。

<div align="center">
  <img src="docs/CMP_In_Native_Architecture.png" width="600" alt="CMP_In_Native_Architecture.png">
</div>

### UI 構成

Compose Multiplatform による UI の共通化は Android と iOS の画面のコンテンツ部分に限定する。

| 方法 |                    | Android   | iOS         |例      |
|------|--------------------|-----------|-------------|-------|
| A    |Scaffold の部品であるヘッダーとコンテンツを CMP で共通化する。|ネイティブ側に Screen コンポーザブル関数と Scaffold を配置して CMP の ヘッダーとコンテンツ を呼び出す。   |ネイティブ側に View protocol を実装した Screen  struct と VStack 等を配置して CMP の ヘッダーとコンテンツ を呼び出す。   |TopScreen | 
| B    |Scafflod のラッパーを CMP で共通化する。|ネイティブ側に Screen コンポーザブル関数を配置し、CMP の Scaffold ラッパーを呼び出す。   |ネイティブ側に View protocol を実装した Screen  struct を配置して Scaffold ラッパーを呼び出す。  |InformationScreen |
| C    |Scafflod のラッパーを CMP で共通化し、ラッパーを画面とする。|ネイティブ側に画面コンポーザブル関数を配置せず、CMP の Scaffold ラッパーを画面として使用する。  |ネイティブ側に View protocol を実装した Screen  struct を配置せず、CMP の Scaffold ラッパーを画面として使用する。   |ViewerScreen(ViewerScaffoldComposeView) |


<div align="center">
  <img src="docs/CMP_In_Native化案-UI共通化.png" width="600" alt="CMP_In_Native化案-UI共通化.png">
</div>

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

