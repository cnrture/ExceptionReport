package com.canerture.exceptionreport.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.canerture.exceptionreport.data.repository.SolutionRepositoryImpl

class SolutionViewModelFactory(private val repository: SolutionRepositoryImpl) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SolutionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SolutionViewModel(repository) as T
        }
        throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
    }
}