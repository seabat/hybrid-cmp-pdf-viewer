package dev.seabat.cmp.pdfviewer.shareddomain.repository

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile

interface PdfListRepositoryContract {
    suspend fun save(pdfList: List<PdfFile>)
    suspend fun read(): List<PdfFile>
}
