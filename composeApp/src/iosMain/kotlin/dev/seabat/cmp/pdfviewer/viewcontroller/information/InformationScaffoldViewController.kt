package dev.seabat.cmp.pdfviewer.viewcontroller.information

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeUIViewController
import dev.seabat.cmp.pdfviewer.screen.information.InformationScaffold
import dev.seabat.cmp.pdfviewer.screen.information.InformationViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.compose.viewmodel.koinViewModel

// Swift の生体認証成功を Composable 内の ViewModel へ橋渡しするチャンネル
private val authSuccessChannel = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

/** Swift 側の生体認証成功コールバックから呼び出す */
fun notifyInformationAuthSuccess() {
    authSuccessChannel.tryEmit(Unit)
}

/**
 * iOS 向けのインフォメーションページ ViewController
 * Kotlin Composable の InformationScaffold を ComposeUIViewController でラップして公開する
 *
 * @param onNavigateBack 戻るボタンをタップしたときに呼ばれるコールバック
 * @param onShowAlert Version 行タップ時に Swift 側のダイアログを表示するコールバック
 * @param onShowBioAuth 生体認証ボタンタップ時に Swift 側の認証プロンプトを表示するコールバック
 * @param onAuthSuccess 生体認証成功時に Swift 側でダイアログを表示するコールバック
 */
fun InformationScaffoldViewController(
    onNavigateBack: () -> Unit,
    onShowAlert: () -> Unit,
    onShowBioAuth: () -> Unit,
    onAuthSuccess: () -> Unit = {}
) = ComposeUIViewController {
    val viewModel: InformationViewModel = koinViewModel()

    LaunchedEffect(Unit) {
        authSuccessChannel.collect {
            viewModel.notifyAuthSuccess()
        }
    }

    InformationScaffold(
        onNavigateBack = onNavigateBack,
        onShowVersionAlert = onShowAlert,
        onShowBioAuth = onShowBioAuth,
        onAuthSuccess = onAuthSuccess
    )
}
