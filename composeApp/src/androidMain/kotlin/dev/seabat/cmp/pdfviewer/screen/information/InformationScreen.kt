package dev.seabat.cmp.pdfviewer.screen.information

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import hypbridcmppdfviewer.composeapp.generated.resources.Res
import hypbridcmppdfviewer.composeapp.generated.resources.alert_ok
import hypbridcmppdfviewer.composeapp.generated.resources.information_alert_message
import hypbridcmppdfviewer.composeapp.generated.resources.information_alert_title
import org.jetbrains.compose.resources.stringResource

/**
 * Android 用のインフォメーションページ
 * Scaffold + TopAppBar で [InformationContent] をラップする
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(onNavigateBack: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(Res.string.information_alert_title)) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) { Text(stringResource(Res.string.alert_ok)) }
            },
            text = { Text(stringResource(Res.string.information_alert_message)) }
        )
    }

    InformationScaffold(
        onNavigateBack = onNavigateBack,
        onShowVersionAlert = { showDialog = true }
    )
}
