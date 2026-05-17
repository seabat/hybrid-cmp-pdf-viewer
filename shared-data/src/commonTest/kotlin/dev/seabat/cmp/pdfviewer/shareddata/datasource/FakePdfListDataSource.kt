package dev.seabat.cmp.pdfviewer.shareddata.datasource

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile

class FakePdfListDataSource : PdfListDataSourceContract {
    private var storage: List<PdfFile> = emptyList()

    override fun save(pdfList: List<PdfFile>) {
        storage = pdfList
    }

    override fun read(): List<PdfFile> = storage
}
