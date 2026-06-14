package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import dev.seabat.cmp.pdfviewer.shareddomain.repository.FakePdfListRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class SavePdfListUseCaseTest {
    private val fakeRepository = FakePdfListRepository()

    @Test
    fun testSavePdfList() = runTest {
        val useCase: SavePdfListUseCaseContract = SavePdfListUseCase(fakeRepository)
        val pdfList = listOf(PdfFile(fileName = "test.pdf", createdAt = "2025-01-01 10:00", size = "1.2 MB"))
        useCase(pdfList)
        assertEquals(pdfList, fakeRepository.read())
    }
}
