package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.repository.FakePdfThumbnailRepository
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest

class RenderPdfThumbnailUseCaseTest {
    private val fakeRepository = FakePdfThumbnailRepository()

    @Test
    fun testRenderPdfThumbnail() = runTest {
        val png = byteArrayOf(10, 20, 30)
        fakeRepository.register("/path/test.pdf", png)
        val useCase: RenderPdfThumbnailUseCaseContract = RenderPdfThumbnailUseCase(fakeRepository)
        assertContentEquals(png, useCase("/path/test.pdf", 96))
    }

    @Test
    fun testRenderPdfThumbnailNotFound() = runTest {
        val useCase: RenderPdfThumbnailUseCaseContract = RenderPdfThumbnailUseCase(fakeRepository)
        assertNull(useCase("/path/missing.pdf", 96))
    }

    @Test
    fun testRenderPdfThumbnailWithEmptyPath() = runTest {
        val useCase: RenderPdfThumbnailUseCaseContract = RenderPdfThumbnailUseCase(fakeRepository)
        assertNull(useCase("", 96))
    }
}
