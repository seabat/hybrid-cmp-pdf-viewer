package dev.seabat.cmp.pdfviewer.shareddata.datasource

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile

interface PdfListDataSourceContract {
    fun save(pdfList: List<PdfFile>)
    fun read(): List<PdfFile>
}
