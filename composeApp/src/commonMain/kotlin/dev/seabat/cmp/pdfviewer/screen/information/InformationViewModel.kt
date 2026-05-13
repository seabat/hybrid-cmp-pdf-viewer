package dev.seabat.cmp.pdfviewer.screen.information

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.seabat.cmp.pdfviewer.shareddomain.usecase.CreatePhrasesUseCaseContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InformationViewModel(
    private val createPhrasesUseCase: CreatePhrasesUseCaseContract
) : ViewModel() {

    private val _phrases = MutableStateFlow<List<String>>(listOf())
    val phrases: StateFlow<List<String>> = _phrases.asStateFlow()

    private val _isAuthSuccess = MutableStateFlow(false)
    val isAuthSuccess: StateFlow<Boolean> = _isAuthSuccess.asStateFlow()

    fun createPhrases() {
       viewModelScope.launch {
           _phrases.value = createPhrasesUseCase()
       }
    }

    fun notifyAuthSuccess() {
        _isAuthSuccess.value = true
    }

    fun resetAuthSuccess() {
        _isAuthSuccess.value = false
    }
}