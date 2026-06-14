package dev.seabat.cmp.pdfviewer.screen.viewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.FindPdfFileByNameUseCaseContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ViewerUiState {
    data object Loading : ViewerUiState
    data class Success(val filePath: String) : ViewerUiState
    data object Error : ViewerUiState
}

class ViewerViewModel(
    private val findPdfFileByNameUseCase: FindPdfFileByNameUseCaseContract
) : ViewModel() {

    private val _uiState = MutableStateFlow<ViewerUiState>(ViewerUiState.Loading)
    val uiState: StateFlow<ViewerUiState> = _uiState.asStateFlow()

    fun loadPdf(fileName: String) {
        viewModelScope.launch {
            val pdfFile = findPdfFileByNameUseCase(fileName)
            _uiState.value = if (pdfFile != null && pdfFile.filePath.isNotEmpty()) {
                ViewerUiState.Success(pdfFile.filePath)
            } else {
                ViewerUiState.Error
            }
        }
    }
}