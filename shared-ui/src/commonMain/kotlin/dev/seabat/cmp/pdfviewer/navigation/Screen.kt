package dev.seabat.cmp.pdfviewer.navigation

sealed interface Screen {
    data object Top : Screen
    data object Information : Screen
    data class Viewer(val fileName: String) : Screen
}
