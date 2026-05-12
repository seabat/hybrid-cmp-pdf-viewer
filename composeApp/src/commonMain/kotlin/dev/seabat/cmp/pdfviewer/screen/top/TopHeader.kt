package dev.seabat.cmp.pdfviewer.screen.top

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import dev.seabat.cmp.pdfviewer.theme.AppColors
import hypbridcmppdfviewer.composeapp.generated.resources.Res
import hypbridcmppdfviewer.composeapp.generated.resources.top_created_at
import hypbridcmppdfviewer.composeapp.generated.resources.top_header
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeader(onNavigateToInformation: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = AppColors.headerContainer.toComposeColor()),
        title = { Text(stringResource(Res.string.top_header)) },
        actions = {
            IconButton(onClick = onNavigateToInformation) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "インフォメーション"
                )
            }
        }
    )
}