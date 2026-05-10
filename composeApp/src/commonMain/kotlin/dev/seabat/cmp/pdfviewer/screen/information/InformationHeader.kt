package dev.seabat.cmp.pdfviewer.screen.information

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import dev.seabat.cmp.pdfviewer.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationHeader(onNavigateBack: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = AppColors.headerContainer.toComposeColor()),
        title = { Text("インフォメーション") },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "戻る"
                )
            }
        }
    )
}
