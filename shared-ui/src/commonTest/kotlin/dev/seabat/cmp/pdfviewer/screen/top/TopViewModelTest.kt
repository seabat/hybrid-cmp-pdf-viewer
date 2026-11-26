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
import kotlin.test.assertNull
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
        deletePdfFileUseCase = FakeDeletePdfFileUseCase(),
        renderPdfThumbnailUseCase = FakeRenderPdfThumbnailUseCase()
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

    @Test
    fun testAddPdfFileWithOpenAfterAddRequestsViewer() = runTest {
        val viewModel = createViewModel(emptyList())
        viewModel.addPdfFile(
            sourceUri = "content://shared/shared.pdf",
            name = "shared.pdf",
            createdAt = "2026-03-01 10:00",
            size = "2KB",
            openAfterAdd = true
        )

        assertEquals(listOf("shared.pdf"), viewModel.pdfList.value.map { it.fileName })
        assertEquals("shared.pdf", viewModel.pendingViewerFileName.value)

        viewModel.consumePendingViewerFileName()
        assertNull(viewModel.pendingViewerFileName.value)
    }

    @Test
    fun testAddPdfFileRenamesDuplicatedFileName() = runTest {
        val viewModel = createViewModel(emptyList())
        repeat(3) {
            viewModel.addPdfFile(
                sourceUri = "content://shared/same.pdf",
                name = "same.pdf",
                createdAt = "2026-03-01 10:00",
                size = "2KB",
                openAfterAdd = true
            )
        }

        // fileName は一覧の key を兼ねるため重複してはならない
        assertEquals(
            listOf("same (1).pdf", "same (2).pdf", "same.pdf"),
            viewModel.pdfList.value.map { it.fileName }.sorted()
        )
        // Viewer は追加されたファイル（連番付き）を開く
        assertEquals("same (2).pdf", viewModel.pendingViewerFileName.value)
    }

    @Test
    fun testAddPdfFileWithoutOpenAfterAddDoesNotRequestViewer() = runTest {
        val viewModel = createViewModel(emptyList())
        viewModel.addPdfFile(
            sourceUri = "content://picked/picked.pdf",
            name = "picked.pdf",
            createdAt = "2026-03-01 10:00",
            size = "2KB"
        )

        assertEquals(listOf("picked.pdf"), viewModel.pdfList.value.map { it.fileName })
        assertNull(viewModel.pendingViewerFileName.value)
    }
}
