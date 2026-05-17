package dev.seabat.cmp.pdfviewer.shareddata.repository

import dev.seabat.cmp.pdfviewer.shareddata.datasource.FakePdfListDataSource
import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class PdfListRepositoryTest {
    private val fakeDataSource = FakePdfListDataSource()
    private val repository = PdfListRepository(fakeDataSource)

    @Test
    fun testSaveAndRead() = runTest {
        val pdfList = listOf(PdfFile(name = "test.pdf", createdAt = "2025-01-01 10:00", size = "1.2 MB"))
        repository.save(pdfList)
        assertEquals(pdfList, repository.read())
    }

    @Test
    fun testReadEmpty() = runTest {
        assertEquals(emptyList(), repository.read())
    }
}
