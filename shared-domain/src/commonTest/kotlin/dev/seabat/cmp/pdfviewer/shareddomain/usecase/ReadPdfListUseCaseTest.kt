package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import dev.seabat.cmp.pdfviewer.shareddomain.repository.FakePdfListRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class ReadPdfListUseCaseTest {
    private val fakeRepository = FakePdfListRepository()

    @Test
    fun testReadPdfListEmpty() = runTest {
        val useCase: ReadPdfListUseCaseContract = ReadPdfListUseCase(fakeRepository)
        assertEquals(emptyList(), useCase())
    }

    @Test
    fun testReadPdfList() = runTest {
        val pdfList = listOf(PdfFile(name = "test.pdf", createdAt = "2025-01-01 10:00", size = "1.2 MB"))
        fakeRepository.save(pdfList)
        val useCase: ReadPdfListUseCaseContract = ReadPdfListUseCase(fakeRepository)
        assertEquals(pdfList, useCase())
    }
}
