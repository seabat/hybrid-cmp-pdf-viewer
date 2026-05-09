package dev.seabat.cmp.pdfviewer.screen.top

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import dev.seabat.cmp.pdfviewer.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeader() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = AppColors.headerContainer.toComposeColor()),
        title = { Text("PDF ビューア") }
    )
}