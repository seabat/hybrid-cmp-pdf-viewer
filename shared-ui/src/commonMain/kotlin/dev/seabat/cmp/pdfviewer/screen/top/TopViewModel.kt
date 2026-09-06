package dev.seabat.cmp.pdfviewer.screen.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.CopyPdfFileUseCaseContract
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.DeletePdfFileUseCaseContract
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.ReadPdfListUseCaseContract
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.SavePdfListUseCaseContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** PDF 一覧の並び替え対象の項目 */
enum class SortField {
    DATE,
    NAME
}

/** PDF 一覧の並び替えの向き */
enum class SortOrder {
    ASC,
    DESC
}

class TopViewModel(
    private val readPdfListUseCase: ReadPdfListUseCaseContract,
    private val savePdfListUseCase: SavePdfListUseCaseContract,
    private val copyPdfFileUseCase: CopyPdfFileUseCaseContract,
    private val deletePdfFileUseCase: DeletePdfFileUseCaseContract
) : ViewModel() {

    private val _rawPdfList = MutableStateFlow<List<PdfFile>>(emptyList())

    private val _sortField = MutableStateFlow(SortField.DATE)
    val sortField: StateFlow<SortField> = _sortField.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.DESC)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _pdfList = MutableStateFlow<List<PdfFile>>(emptyList())
    val pdfList: StateFlow<List<PdfFile>> = _pdfList.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _rawPdfList.value = readPdfListUseCase()
                applySort()
            } catch (_: Exception) {
            }
        }
    }

    fun addPdfFile(sourceUri: String, name: String, createdAt: String, size: String) {
        viewModelScope.launch {
            try {
                val filePath = copyPdfFileUseCase(sourceUri = sourceUri, destFileName = name)
                val newList = _rawPdfList.value + PdfFile(
                    fileName = name,
                    createdAt = createdAt,
                    size = size,
                    filePath = filePath
                )
                _rawPdfList.value = newList
                applySort()
                savePdfListUseCase(newList)
            } catch (_: Exception) {
            }
        }
    }

    fun deletePdfFile(file: PdfFile) {
        viewModelScope.launch {
            try {
                deletePdfFileUseCase(file.filePath)
                val newList = _rawPdfList.value - file
                _rawPdfList.value = newList
                applySort()
                savePdfListUseCase(newList)
            } catch (_: Exception) {
            }
        }
    }

    fun onSortOptionSelected(field: SortField, order: SortOrder) {
        _sortField.value = field
        _sortOrder.value = order
        applySort()
    }

    private fun applySort() {
        val comparator = when (_sortField.value) {
            SortField.DATE -> compareBy<PdfFile> { it.createdAt }
            SortField.NAME -> compareBy { it.displayName.ifEmpty { it.fileName } }
        }
        val sorted = _rawPdfList.value.sortedWith(comparator)
        _pdfList.value = if (_sortOrder.value == SortOrder.DESC) sorted.asReversed() else sorted
    }
}
