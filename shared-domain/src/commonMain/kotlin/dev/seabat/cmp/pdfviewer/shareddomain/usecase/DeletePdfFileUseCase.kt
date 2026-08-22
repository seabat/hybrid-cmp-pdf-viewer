package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.repository.PdfFileRepositoryContract

class DeletePdfFileUseCase(
    private val pdfFileRepository: PdfFileRepositoryContract
) : DeletePdfFileUseCaseContract {
    override suspend fun invoke(filePath: String) = pdfFileRepository.delete(filePath)
}
