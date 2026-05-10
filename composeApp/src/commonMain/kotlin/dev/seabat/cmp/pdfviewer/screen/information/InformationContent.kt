package dev.seabat.cmp.pdfviewer.screen.information

import androidx.compose.foundation.background
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
import dev.seabat.cmp.pdfviewer.screen.viewer.ViewerViewModel
import dev.seabat.cmp.pdfviewer.theme.AppColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InformationContent(
    modifier: Modifier = Modifier
) {
    val viewModel: ViewerViewModel = koinViewModel()
    val phrases by viewModel.phrases.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.createPhrases()
    }

    Column(
        modifier = modifier.background(color = AppColors.contentContainer.toComposeColor()).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalDivider()
        phrases.forEach { phrase ->
            Text(phrase)
            HorizontalDivider()
        }
    }
}
