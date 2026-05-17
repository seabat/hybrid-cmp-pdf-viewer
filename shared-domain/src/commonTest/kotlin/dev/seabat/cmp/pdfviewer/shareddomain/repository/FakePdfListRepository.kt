package dev.seabat.cmp.pdfviewer.shareddomain.repository

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile

class FakePdfListRepository : PdfListRepositoryContract {
    private var storage: List<PdfFile> = emptyList()

    override suspend fun save(pdfList: List<PdfFile>) {
        storage = pdfList
    }

    override suspend fun read(): List<PdfFile> = storage
}
