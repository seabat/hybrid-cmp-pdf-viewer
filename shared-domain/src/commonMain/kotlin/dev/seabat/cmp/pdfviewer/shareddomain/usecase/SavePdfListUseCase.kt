package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import dev.seabat.cmp.pdfviewer.shareddomain.repository.PdfListRepositoryContract

class SavePdfListUseCase(
    private val pdfListRepository: PdfListRepositoryContract
) : SavePdfListUseCaseContract {
    override suspend fun invoke(pdfList: List<PdfFile>) {
        pdfListRepository.save(pdfList)
    }
}
