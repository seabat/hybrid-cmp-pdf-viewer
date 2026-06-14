package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile

interface FindPdfFileByNameUseCaseContract {
    suspend operator fun invoke(fileName: String): PdfFile?
}