package dev.seabat.cmp.pdfviewer.screen.information

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.seabat.cmp.pdfviewer.theme.AppColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InformationContent(
    modifier: Modifier = Modifier,
    onVersionTapped: () -> Unit = {},
    onShowBioAuth: () -> Unit = {},
    onAuthSuccess: () -> Unit = {}
) {
    val viewModel: InformationViewModel = koinViewModel()
    val phrases by viewModel.phrases.collectAsStateWithLifecycle()
    val isAuthSuccess by viewModel.isAuthSuccess.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.createPhrases()
    }

    LaunchedEffect(isAuthSuccess) {
        if (isAuthSuccess) {
            onAuthSuccess()
            viewModel.resetAuthSuccess()
        }
    }

    Column(
        modifier = modifier.background(color = AppColors.contentContainer.toComposeColor()).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalDivider()
        phrases.forEach { phrase ->
            if (phrase.contains("Version:")) {
                Text(phrase, modifier = Modifier.clickable { onVersionTapped() })
            } else if (phrase.contains("OS:")) {
                Text(phrase, modifier = Modifier.clickable { onShowBioAuth() })
            } else {
                Text(phrase)
            }
            HorizontalDivider()
        }
    }
}
