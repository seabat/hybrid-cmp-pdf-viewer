package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.repository.PdfFileRepositoryContract

class CopyPdfFileUseCase(
    private val pdfFileRepository: PdfFileRepositoryContract
) : CopyPdfFileUseCaseContract {
    override suspend fun invoke(sourceUri: String, destFileName: String): String =
        pdfFileRepository.copyToInternalStorage(sourceUri, destFileName)
}