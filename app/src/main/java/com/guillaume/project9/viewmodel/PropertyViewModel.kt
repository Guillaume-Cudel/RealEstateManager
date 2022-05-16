package com.guillaume.project9.viewmodel

import androidx.lifecycle.*
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.Property
import com.guillaume.project9.repository.PropertyRepository
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PropertyViewModel(private val repository: PropertyRepository): ViewModel() {


    val allPropertys: LiveData<List<Property>> = repository.allPropertys.asLiveData()
    //val allPhotosByProperty: LiveData<List<Photo>> = repository

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertProperty(property: Property) = viewModelScope.launch {
        repository.insertProperty(property)
    }

    fun insertPhotos(photos: List<Photo?>) = viewModelScope.launch {
        repository.insertPhotos(photos)
    }

    fun getPhotosByProperty(propertyId: String?): LiveData<List<Photo>> {
        //val result = MutableLiveData<List<Photo>>()
        var photoList: LiveData<List<Photo>>? = null
        viewModelScope.launch {
            photoList = repository.getPhotosByProperty(propertyId).asLiveData()
            //result.postValue(photoList)
        }
        return photoList!!
    }






}