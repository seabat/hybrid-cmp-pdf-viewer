package dev.seabat.cmp.pdfviewer.shareddata.repository

import dev.seabat.cmp.pdfviewer.shareddata.datasource.PdfListDataSourceContract
import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import dev.seabat.cmp.pdfviewer.shareddomain.repository.PdfListRepositoryContract

class PdfListRepository(
    private val dataSource: PdfListDataSourceContract
) : PdfListRepositoryContract {
    override suspend fun save(pdfList: List<PdfFile>) {
        dataSource.save(pdfList)
    }

    override suspend fun read(): List<PdfFile> = dataSource.read()
}
