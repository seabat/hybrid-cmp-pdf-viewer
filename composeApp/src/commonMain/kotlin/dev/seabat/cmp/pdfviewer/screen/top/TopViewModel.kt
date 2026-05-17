package dev.seabat.cmp.pdfviewer.screen.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.ReadPdfListUseCaseContract
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.SavePdfListUseCaseContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TopViewModel(
    private val readPdfListUseCase: ReadPdfListUseCaseContract,
    private val savePdfListUseCase: SavePdfListUseCaseContract
) : ViewModel() {

    private val _pdfList = MutableStateFlow<List<PdfFile>>(emptyList())
    val pdfList: StateFlow<List<PdfFile>> = _pdfList.asStateFlow()

    init {
        viewModelScope.launch {
            _pdfList.value = readPdfListUseCase()
        }
    }

    fun addPdfFile(name: String, createdAt: String, size: String) {
        viewModelScope.launch {
            val newList = _pdfList.value + PdfFile(name = name, createdAt = createdAt, size = size)
            _pdfList.value = newList
            savePdfListUseCase(newList)
        }
    }
}
