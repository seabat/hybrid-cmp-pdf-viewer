package dev.seabat.cmp.pdfviewer.screen.top

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TopViewModelTest {

    private val oldJpgLike = PdfFile(
        fileName = "b.pdf",
        displayName = "Banana",
        createdAt = "2026-01-01 10:00",
        size = "1KB"
    )
    private val newFile = PdfFile(
        fileName = "a.pdf",
        displayName = "Apple",
        createdAt = "2026-02-01 10:00",
        size = "1KB"
    )

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(initialList: List<PdfFile>): TopViewModel = TopViewModel(
        readPdfListUseCase = FakeReadPdfListUseCase(initialList),
        savePdfListUseCase = FakeSavePdfListUseCase(),
        copyPdfFileUseCase = FakeCopyPdfFileUseCase(),
        deletePdfFileUseCase = FakeDeletePdfFileUseCase()
    )

    @Test
    fun testInitialSortIsDateDescending() = runTest {
        val viewModel = createViewModel(listOf(oldJpgLike, newFile))
        assertEquals(listOf(newFile, oldJpgLike), viewModel.pdfList.value)
    }

    @Test
    fun testSortByDateAscending() = runTest {
        val viewModel = createViewModel(listOf(oldJpgLike, newFile))
        viewModel.selectSortOption(SortField.DATE, SortOrder.ASC)
        assertEquals(listOf(oldJpgLike, newFile), viewModel.pdfList.value)
    }

    @Test
    fun testSortByNameAscending() = runTest {
        val viewModel = createViewModel(listOf(oldJpgLike, newFile))
        viewModel.selectSortOption(SortField.NAME, SortOrder.ASC)
        // Apple, Banana
        assertEquals(listOf(newFile, oldJpgLike), viewModel.pdfList.value)
    }

    @Test
    fun testSortByNameDescending() = runTest {
        val viewModel = createViewModel(listOf(oldJpgLike, newFile))
        viewModel.selectSortOption(SortField.NAME, SortOrder.DESC)
        // Banana, Apple
        assertEquals(listOf(oldJpgLike, newFile), viewModel.pdfList.value)
    }

    @Test
    fun testSortSheetVisibility() = runTest {
        val viewModel = createViewModel(emptyList())
        assertFalse(viewModel.isSortSheetVisible.value)

        viewModel.showSortSheet()
        assertTrue(viewModel.isSortSheetVisible.value)

        viewModel.dismissSortSheet()
        assertFalse(viewModel.isSortSheetVisible.value)
    }

    @Test
    fun testSortSheetClosesAfterOptionSelected() = runTest {
        val viewModel = createViewModel(emptyList())
        viewModel.showSortSheet()
        viewModel.selectSortOption(SortField.DATE, SortOrder.ASC)
        assertFalse(viewModel.isSortSheetVisible.value)
    }
}
