package com.example.repsync.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.repsync.repository.FitnessRepository

class FitViewModelFactory(
    private val repository: FitnessRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(FitViewModel::class.java)) {
            return FitViewModel(repository) as T
        }
        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}