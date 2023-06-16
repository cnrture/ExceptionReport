package com.canerture.exceptionreport.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.exceptionreport.common.Resource
import com.canerture.exceptionreport.data.repository.SolutionRepositoryImpl
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SolutionViewModel(
    private val solutionRepositoryImpl: SolutionRepositoryImpl
) : ViewModel() {

    private val _state: MutableStateFlow<SolutionState> = MutableStateFlow(SolutionState())
    val state = _state.asStateFlow()

    private val _effect: MutableSharedFlow<SolutionEffect> = MutableSharedFlow()
    val effect = _effect.asSharedFlow()

    fun getSuggestionResponse(content: String) = viewModelScope.launch {
        _state.emit(SolutionState(true))
        when (val response = solutionRepositoryImpl.getSuggestionResponse(content)) {
            is Resource.Success -> {
                _state.emit(SolutionState(false, response.data))
            }

            is Resource.Error -> {
                _state.emit(SolutionState(false))
                _effect.emit(SolutionEffect.ShowError(response.message))
            }
        }
    }
}

data class SolutionState(
    val isLoading: Boolean = false,
    val solution: String? = null
)

sealed interface SolutionEffect {
    data class ShowError(val message: String) : SolutionEffect
}