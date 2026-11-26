package dev.seabat.cmp.pdfviewer

import android.net.Uri
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.seabat.cmp.pdfviewer.screen.information.InformationScreen
import dev.seabat.cmp.pdfviewer.screen.top.TopScreen
import dev.seabat.cmp.pdfviewer.screen.viewer.ViewerScaffold

/**
 * Android アプリのルートコンポーザブル
 * Activity の NavHost 内で画面遷移を管理する
 *
 * @param incomingPdfUri 他アプリから「開く」「共有」で渡された PDF の Uri 文字列
 * @param onIncomingPdfConsumed [incomingPdfUri] の処理が済んだことを通知するコールバック
 */
@Composable
fun NavigationApp(
    incomingPdfUri: String? = null,
    onIncomingPdfConsumed: () -> Unit = {}
) {
    val navController = rememberNavController()

    // PDF を受け取ったときに Viewer / Information を表示中だった場合、
    // 追加処理を行うトップページまで戻す（トップページが未表示だと PDF が処理されない）
    LaunchedEffect(incomingPdfUri) {
        if (incomingPdfUri != null) {
            navController.popBackStack(route = "top", inclusive = false)
        }
    }

    MaterialTheme {
        NavHost(navController = navController, startDestination = "top") {
            // トップページ
            composable("top") {
                TopScreen(
                    onNavigateToViewer = { fileName ->
                        // 空白や記号を含むファイル名でもルートが壊れないようエンコードする
                        // （取り出し側は Navigation がデコードするため加工不要）
                        navController.navigate("viewer/${Uri.encode(fileName)}")
                    },
                    onNavigateToInformation = {
                        navController.navigate("information")
                    },
                    incomingPdfUri = incomingPdfUri,
                    onIncomingPdfConsumed = onIncomingPdfConsumed
                )
            }

            // インフォメーションページ
            composable("information") {
                InformationScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // ビューアページ
            composable("viewer/{fileName}") { backStackEntry ->
                val fileName = backStackEntry.arguments?.getString("fileName") ?: ""
                ViewerScaffold(
                    fileName = fileName,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
