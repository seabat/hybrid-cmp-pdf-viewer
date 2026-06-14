package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import dev.seabat.cmp.pdfviewer.shareddomain.repository.PdfListRepositoryContract

class FindPdfFileByNameUseCase(
    private val pdfListRepository: PdfListRepositoryContract
) : FindPdfFileByNameUseCaseContract {
    override suspend fun invoke(fileName: String): PdfFile? =
        pdfListRepository.read().find { it.fileName == fileName }
}