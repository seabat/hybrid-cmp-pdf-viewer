# ユースケース追加ルール

本プロジェクトは UI Layer - Domain Layer - Data Layer のクリーンアーキテクチャを採用している。
参考: https://developer.android.com/topic/architecture

```
UI Layer       composeApp
Domain Layer   shared-domain
Data Layer     shared-data
```

---

## モジュール構成と配置先

| 種別 | モジュール | パッケージ |
|-----|-----------|-----------|
| ユースケース（インタフェース・実装） | shared-domain | `shareddomain/usecase/` |
| エンティティ | shared-domain | `shareddomain/entity/` |
| リポジトリ インタフェース | shared-domain | `shareddomain/repository/` |
| リポジトリ 実装 | shared-data | `shareddata/repository/` |
| データソース インタフェース | shared-data commonMain | `shareddata/datasource/` |
| データソース 実装（共通） | shared-data commonMain | `shareddata/datasource/` |
| データソース 実装（Android） | shared-data androidMain | `shareddata/datasource/` |
| データソース 実装（iOS） | shared-data iosMain | `shareddata/datasource/` |

---

## シナリオ別 追加ファイルセット

### シナリオ1：ユースケースのみ追加（既存リポジトリを利用）

```
shared-domain/src/commonMain/.../shareddomain/usecase/
├── {UseCaseName}UseCaseContract.kt   # インタフェース
└── {UseCaseName}UseCase.kt           # 実装

shared-domain/src/commonTest/.../shareddomain/usecase/
└── {UseCaseName}UseCaseTest.kt       # ユニットテスト
```

DI 更新: `DomainModule.kt` の `useCaseModule` に追加。

---

### シナリオ2：ユースケース + リポジトリを追加

シナリオ1 のファイルに加えて：

```
shared-domain/src/commonMain/.../shareddomain/repository/
└── {RepositoryName}RepositoryContract.kt   # インタフェース

shared-data/src/commonMain/.../shareddata/repository/
└── {RepositoryName}Repository.kt           # 実装

shared-data/src/commonTest/.../shareddata/repository/
└── {RepositoryName}RepositoryTest.kt       # ユニットテスト
```

DI 更新:
- `DomainModule.kt` → `useCaseModule` にユースケースを追加
- `DataModule.kt` → `repositoryModule` にリポジトリを追加

---

### シナリオ3：ユースケース + リポジトリ + データソースを追加（プラットフォーム固有の実装）

シナリオ2 のファイルに加えて：

```
shared-data/src/commonMain/.../shareddata/datasource/
├── {DataSourceName}DataSourceContract.kt   # インタフェース
└── {DataSourceName}DataSource.kt           # expect クラス（宣言のみ）

shared-data/src/androidMain/.../shareddata/datasource/
└── {DataSourceName}DataSource.kt           # actual クラス（Android 実装）

shared-data/src/iosMain/.../shareddata/datasource/
└── {DataSourceName}DataSource.kt           # actual クラス（iOS 実装）

shared-data/src/commonTest/.../shareddata/datasource/
└── Fake{DataSourceName}DataSource.kt       # テスト用 Fake

shared-domain/src/commonTest/.../shareddomain/repository/
└── Fake{RepositoryName}Repository.kt       # ユースケーステスト用 Fake
```

DI 更新:
- `DomainModule.kt` → `useCaseModule` にユースケースを追加
- `DataModule.kt` → `repositoryModule` にリポジトリを追加、`dataSourceModule` にデータソースを追加

---

### シナリオ4：既存ユースケースに新しいリポジトリ依存を追加

新規ユースケースではなく、既存ユースケースのコンストラクタに新しいリポジトリ依存を追加するケース。

追加するリポジトリ・データソースのファイルはシナリオ2またはシナリオ3と同じ。  
加えて以下を **更新** する：

```
# 既存のユースケース実装を更新
shared-domain/src/commonMain/.../shareddomain/usecase/
└── {ExistingUseCaseName}UseCase.kt   # コンストラクタに新しい依存を追加

# 既存のユースケーステストを更新
shared-domain/src/commonTest/.../shareddomain/usecase/
└── {ExistingUseCaseName}UseCaseTest.kt   # 新しい Fake を引数に追加、アサーションを更新
```

DI 更新:
- `DomainModule.kt` → 既存の `single<...> { ExistingUseCase(get()) }` を `single<...> { ExistingUseCase(get(), get()) }` のように引数を追加
- `DataModule.kt` → `repositoryModule` / `dataSourceModule` に新規コンポーネントを追加

---

## Koin モジュールの更新

コンポーネントを追加したら、必ず対応する Koin モジュールに登録する。

### 登録先ファイルと対象コンポーネント

| 追加したコンポーネント | 登録先ファイル | 登録先モジュール変数 |
|----------------------|--------------|-------------------|
| ユースケース | `shared-domain/src/commonMain/.../shareddomain/di/DomainModule.kt` | `useCaseModule` |
| リポジトリ | `shared-data/src/commonMain/.../shareddata/di/DataModule.kt` | `repositoryModule` |
| データソース | `shared-data/src/commonMain/.../shareddata/di/DataModule.kt` | `dataSourceModule` |

### 登録方法（既存モジュールに追記）

参照: `shared-domain/.../di/DomainModule.kt`, `shared-data/.../di/DataModule.kt`

```kotlin
// DomainModule.kt — ユースケースを追加
val useCaseModule = module {
    single<CreatePhrasesUseCaseContract> { CreatePhrasesUseCase(get()) }
    single<{UseCaseName}UseCaseContract> { {UseCaseName}UseCase(get()) }  // 追記
}
```

```kotlin
// DataModule.kt — リポジトリ・データソースを追加
val repositoryModule = module {
    single<PlatformRepositoryContract> { PlatformRepository(get()) }
    single<{RepositoryName}RepositoryContract> { {RepositoryName}Repository(get()) }  // 追記
}

val dataSourceModule = module {
    single<PlatformInfoDataSourceContract> { PlatformInfoDataSource() }
    single<{DataSourceName}DataSourceContract> { {DataSourceName}DataSource() }  // 追記
}
```

> `get()` はコンストラクタ引数を Koin が自動解決するための記述。依存するコンポーネントが先に登録されている必要はない（Koin が遅延解決する）。

---

## 命名規約

| 種別 | パターン | 例 |
|-----|---------|---|
| ユースケース インタフェース | `{Name}UseCaseContract` | `CreatePhrasesUseCaseContract` |
| ユースケース 実装 | `{Name}UseCase` | `CreatePhrasesUseCase` |
| リポジトリ インタフェース | `{Name}RepositoryContract` | `PlatformRepositoryContract` |
| リポジトリ 実装 | `{Name}Repository` | `PlatformRepository` |
| データソース インタフェース | `{Name}DataSourceContract` | `PlatformInfoDataSourceContract` |
| データソース 実装 | `{Name}DataSource` | `PlatformInfoDataSource` |
| テスト用 Fake | `Fake{Name}` | `FakePlatformRepository`, `FakePlatformInfoDataSource` |

---

## 実装パターン（参照例）

### ユースケース インタフェース
参照: `shared-domain/.../usecase/CreatePhrasesUseCaseContract.kt`
```kotlin
interface {UseCaseName}UseCaseContract {
    suspend operator fun invoke(): {ReturnType}
}
```

### ユースケース 実装
参照: `shared-domain/.../usecase/CreatePhrasesUseCase.kt`
```kotlin
// リポジトリが1つの場合
class {UseCaseName}UseCase(private val repository: {RepositoryName}RepositoryContract) :
    {UseCaseName}UseCaseContract {
    override suspend fun invoke(): {ReturnType} { ... }
}

// リポジトリが複数の場合（コンストラクタに必要な数だけ追加）
class {UseCaseName}UseCase(
    private val repository: {RepositoryName}RepositoryContract,
    private val otherRepository: {OtherRepositoryName}RepositoryContract
) : {UseCaseName}UseCaseContract {
    override suspend fun invoke(): {ReturnType} { ... }
}
```

### リポジトリ インタフェース
参照: `shared-domain/.../repository/PlatformRepositoryContract.kt`
```kotlin
interface {RepositoryName}RepositoryContract {
    fun {methodName}(): {ReturnType}
}
```

### リポジトリ 実装
参照: `shared-data/.../repository/PlatformRepository.kt`
```kotlin
class {RepositoryName}Repository(private val dataSource: {DataSourceName}DataSourceContract) :
    {RepositoryName}RepositoryContract {
    override fun {methodName}(): {ReturnType} = dataSource.{method}()
}
```

### データソース（expect/actual パターン）
参照: `shared-data/.../datasource/PlatformInfoDataSource.kt`

commonMain（宣言）:
```kotlin
expect class {DataSourceName}DataSource() : {DataSourceName}DataSourceContract
```

androidMain / iosMain（実装）:
```kotlin
actual class {DataSourceName}DataSource actual constructor() : {DataSourceName}DataSourceContract {
    override fun {methodName}(): {ReturnType} { /* プラットフォーム固有の実装 */ }
}
```

### DI 登録
参照: `shared-domain/.../di/DomainModule.kt`, `shared-data/.../di/DataModule.kt`
```kotlin
// DomainModule.kt
val useCaseModule = module {
    single<{UseCaseName}UseCaseContract> { {UseCaseName}UseCase(get()) }
}

// DataModule.kt
val repositoryModule = module {
    single<{RepositoryName}RepositoryContract> { {RepositoryName}Repository(get()) }
}
val dataSourceModule = module {
    single<{DataSourceName}DataSourceContract> { {DataSourceName}DataSource() }
}
```

### ユニットテスト
参照: `shared-domain/.../usecase/CreatePhrasesUseCaseTest.kt`
```kotlin
class {UseCaseName}UseCaseTest {
    private lateinit var useCase: {UseCaseName}UseCaseContract

    @Test
    fun test{UseCaseName}() {
        useCase = {UseCaseName}UseCase(Fake{RepositoryName}Repository())
        runTest {
            val result = useCase()
            // assertions
        }
    }
}
```

### テスト用 Fake
参照: `shared-domain/.../repository/FakePlatformRepository.kt`
```kotlin
class Fake{RepositoryName}Repository : {RepositoryName}RepositoryContract {
    override fun {methodName}(): {ReturnType} = /* ダミー値 */
}
```
