package dev.seabat.cmp.pdfviewer.screen.viewer

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
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerHeader(fileName: String, onNavigateBack: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = AppColors.headerContainer.toComposeColor(),
            titleContentColor = AppColors.headerContent.toComposeColor(),
            navigationIconContentColor = AppColors.headerContent.toComposeColor()
        ),
        title = { Text(fileName) },
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

@Preview
@Composable
fun ViewerHeaderPreview() {
    ViewerHeader(fileName = "sample.pdf", onNavigateBack = {})
}