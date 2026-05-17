package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import dev.seabat.cmp.pdfviewer.shareddomain.repository.PdfListRepositoryContract

class ReadPdfListUseCase(
    private val pdfListRepository: PdfListRepositoryContract
) : ReadPdfListUseCaseContract {
    override suspend fun invoke(): List<PdfFile> = pdfListRepository.read()
}
