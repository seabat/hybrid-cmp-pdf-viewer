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
import dev.seabat.cmp.pdfviewer.sharedui.generated.resources.Res
import dev.seabat.cmp.pdfviewer.sharedui.generated.resources.information_header
import dev.seabat.cmp.pdfviewer.sharedui.generated.resources.top_header
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationHeader(onNavigateBack: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = AppColors.headerContainer.toComposeColor(),
            titleContentColor = AppColors.headerContent.toComposeColor(),
            navigationIconContentColor = AppColors.headerContent.toComposeColor()
        ),
        title = { Text(stringResource(Res.string.information_header)) },
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
fun InformationHeaderPreview() {
    InformationHeader(onNavigateBack = {})
}
