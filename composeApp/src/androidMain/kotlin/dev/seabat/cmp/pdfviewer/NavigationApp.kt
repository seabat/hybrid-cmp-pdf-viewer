package dev.seabat.cmp.pdfviewer

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.seabat.cmp.pdfviewer.screen.information.InformationScreen
import dev.seabat.cmp.pdfviewer.screen.top.TopScreen
import dev.seabat.cmp.pdfviewer.screen.viewer.ViewerScreen

/**
 * Android アプリのルートコンポーザブル
 * Activity の NavHost 内で画面遷移を管理する
 */
@Composable
fun NavigationApp() {
    val navController = rememberNavController()

    MaterialTheme {
        NavHost(navController = navController, startDestination = "top") {
            // トップページ
            composable("top") {
                TopScreen(
                    onNavigateToViewer = { fileName ->
                        navController.navigate("viewer/$fileName")
                    },
                    onNavigateToInformation = {
                        navController.navigate("information")
                    }
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
                ViewerScreen(
                    fileName = fileName,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
