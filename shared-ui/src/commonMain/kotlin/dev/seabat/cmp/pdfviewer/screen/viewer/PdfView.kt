package dev.seabat.cmp.pdfviewer.screen.viewer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PdfView(filePath: String, modifier: Modifier = Modifier)