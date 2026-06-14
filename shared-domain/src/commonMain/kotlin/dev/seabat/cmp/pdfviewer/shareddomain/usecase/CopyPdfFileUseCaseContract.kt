package dev.seabat.cmp.pdfviewer.shareddomain.usecase

interface CopyPdfFileUseCaseContract {
    suspend operator fun invoke(sourceUri: String, destFileName: String): String
}