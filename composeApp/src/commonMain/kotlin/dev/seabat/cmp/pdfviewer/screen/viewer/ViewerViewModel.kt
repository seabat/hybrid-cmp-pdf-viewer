package dev.seabat.cmp.pdfviewer.screen.viewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.CreatePhrasesUseCaseContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewerViewModel(
    private val createPhrasesUseCase: CreatePhrasesUseCaseContract
) : ViewModel() {

    private val _phrases = MutableStateFlow<List<String>>(listOf())
    val phrases: StateFlow<List<String>> = _phrases.asStateFlow()

    fun createPhrases() {
       viewModelScope.launch {
           _phrases.value = createPhrasesUseCase()
       }
    }
}