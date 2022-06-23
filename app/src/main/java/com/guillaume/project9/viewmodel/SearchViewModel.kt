package com.guillaume.project9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.guillaume.project9.model.Property
import com.guillaume.project9.repository.PropertyRepository
import kotlinx.coroutines.launch

class SearchViewModel(val repository: PropertyRepository): ViewModel() {

    fun searchPropertysWithConditions(kind: String, priceMin: Int, priceMax: Int,
                                      surfaceMin: Double, surfaceMax: Double): LiveData<MutableList<Property>> {
        var searchList: LiveData<MutableList<Property>>? = null
        viewModelScope.launch {
            searchList = repository.searchPropertysWithConditions(kind, priceMin, priceMax, surfaceMin, surfaceMax).asLiveData()
        }
        return searchList!!
    }
}