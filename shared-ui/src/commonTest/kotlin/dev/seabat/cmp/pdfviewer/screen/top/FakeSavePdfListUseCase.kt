package dev.seabat.cmp.pdfviewer.screen.top

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.SavePdfListUseCaseContract

class FakeSavePdfListUseCase : SavePdfListUseCaseContract {
    var savedList: List<PdfFile>? = null
        private set

    override suspend fun invoke(pdfList: List<PdfFile>) {
        savedList = pdfList
    }
}
