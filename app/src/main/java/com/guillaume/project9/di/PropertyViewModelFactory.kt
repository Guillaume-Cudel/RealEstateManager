package com.guillaume.project9.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guillaume.project9.repository.PropertyRepository
import com.guillaume.project9.viewmodel.PropertyViewModel

class PropertyViewModelFactory(private val repository: PropertyRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PropertyViewModel::class.java)){
            @Suppress("UNCHEKED_CAST")
            return PropertyViewModel(repository) as T
        }
        //todo add other if condition to add other viewModel (and other repository in parameter)

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}