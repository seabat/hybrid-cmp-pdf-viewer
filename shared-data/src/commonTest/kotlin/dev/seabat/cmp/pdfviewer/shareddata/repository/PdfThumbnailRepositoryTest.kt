package dev.seabat.cmp.pdfviewer.shareddata.repository

import dev.seabat.cmp.pdfviewer.shareddata.datasource.FakePdfThumbnailDataSource
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest

class PdfThumbnailRepositoryTest {
    private val fakeDataSource = FakePdfThumbnailDataSource()
    private val repository = PdfThumbnailRepository(fakeDataSource)

    @Test
    fun testRenderFirstPage() = runTest {
        val png = byteArrayOf(1, 2, 3)
        fakeDataSource.register("/path/test.pdf", png)
        assertContentEquals(png, repository.renderFirstPage("/path/test.pdf", 96))
    }

    @Test
    fun testRenderFirstPageNotFound() = runTest {
        assertNull(repository.renderFirstPage("/path/missing.pdf", 96))
    }
}
