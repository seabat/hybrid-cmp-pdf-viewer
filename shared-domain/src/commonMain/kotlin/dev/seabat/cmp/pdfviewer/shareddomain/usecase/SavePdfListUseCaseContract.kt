package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile

interface SavePdfListUseCaseContract {
    suspend operator fun invoke(pdfList: List<PdfFile>)
}
