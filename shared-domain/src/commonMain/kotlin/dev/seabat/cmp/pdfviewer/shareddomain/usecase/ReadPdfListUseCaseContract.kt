package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile

interface ReadPdfListUseCaseContract {
    suspend operator fun invoke(): List<PdfFile>
}
