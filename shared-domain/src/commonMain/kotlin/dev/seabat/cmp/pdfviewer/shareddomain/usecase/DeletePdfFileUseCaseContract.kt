package dev.seabat.cmp.pdfviewer.shareddomain.usecase

interface DeletePdfFileUseCaseContract {
    suspend operator fun invoke(filePath: String)
}
