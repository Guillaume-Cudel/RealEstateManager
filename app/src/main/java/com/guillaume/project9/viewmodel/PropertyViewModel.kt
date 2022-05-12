package com.guillaume.project9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.Property
import com.guillaume.project9.repository.PropertyRepository
import kotlinx.coroutines.launch

class PropertyViewModel(private val repository: PropertyRepository): ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    val allPropertys: LiveData<List<Property>> = repository.allPropertys.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertProperty(property: Property) = viewModelScope.launch {
        repository.insertProperty(property)
    }

    fun insertPhoto(photo: Photo) = viewModelScope.launch {
        repository.insertPhoto(photo)
    }


}