package dev.seabat.cmp.pdfviewer.viewcontroller.information

import androidx.compose.ui.window.ComposeUIViewController
import dev.seabat.cmp.pdfviewer.screen.information.InformationScaffold

/**
 * iOS 向けのインフォメーションページ ViewController
 * Kotlin Composable の InformationScaffold を ComposeUIViewController でラップして公開する
 *
 * @param onNavigateBack 戻るボタンをタップしたときに呼ばれるコールバック
 * @param onShowAlert Version 行タップ時に Swift 側のダイアログを表示するコールバック
 */
fun InformationScaffoldViewController(
    onNavigateBack: () -> Unit,
    onShowAlert: () -> Unit,
    onShowBioAuth: () -> Unit
) = ComposeUIViewController {
    InformationScaffold(
        onNavigateBack = onNavigateBack,
        onShowVersionAlert = onShowAlert,
        onShowBioAuth = onShowBioAuth
    )
}
