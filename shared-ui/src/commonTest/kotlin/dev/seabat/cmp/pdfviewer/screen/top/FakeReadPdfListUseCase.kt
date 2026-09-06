package dev.seabat.cmp.pdfviewer.screen.top

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.ReadPdfListUseCaseContract

class FakeReadPdfListUseCase(private val pdfList: List<PdfFile> = emptyList()) : ReadPdfListUseCaseContract {
    override suspend fun invoke(): List<PdfFile> = pdfList
}
