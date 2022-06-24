package com.guillaume.project9.viewmodel

import androidx.lifecycle.*
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.PointsOfInterest
import com.guillaume.project9.model.Property
import com.guillaume.project9.repository.PropertyRepository
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PropertyViewModel(private val repository: PropertyRepository): ViewModel() {


    val allPropertys: LiveData<List<Property>> = repository.allPropertys.asLiveData()

    fun insertProperty(property: Property) = viewModelScope.launch {
        repository.insertProperty(property)
    }

    fun insertPhotos(photos: List<Photo?>) = viewModelScope.launch {
        repository.insertPhotos(photos)
    }

    fun insertPointsOfInterest(pointsOfInterest: List<PointsOfInterest?>) = viewModelScope.launch {
        repository.insertPointOfInterest(pointsOfInterest)
    }

    fun getPhotosByProperty(propertyId: String?): LiveData<List<Photo>> {
        var liveData: LiveData<List<Photo>>? = null
        viewModelScope.launch {
            liveData = repository.getPhotosByProperty(propertyId).asLiveData()
        }
        return liveData!!
    }

    fun getPointsOfInterestByProperty(propertyId: String?): LiveData<List<PointsOfInterest>>{
        var pointsOfInterest: LiveData<List<PointsOfInterest>>? = null
        viewModelScope.launch {
            pointsOfInterest = repository.getPointsOfInterest(propertyId).asLiveData()
        }
        return pointsOfInterest!!
    }

    /*private val _listInterestLiveData = MutableLiveData<List<PointsOfInterest>>()
    val listInterestLiveData: LiveData<List<PointsOfInterest>> = _listInterestLiveData
    fun getPointsOfInterestByProperty(propertyId: String?){
        viewModelScope.launch {
            repository.getPointsOfInterest(propertyId).collect {
                _listInterestLiveData.postValue(it)
            }
        }
    }*/


    fun updateProperty(property: Property) = viewModelScope.launch {
        repository.updateProperty(property)
    }

    fun deletePhotos(propertyId: String?) = viewModelScope.launch {
        repository.deletePhotos(propertyId)
    }

    fun deleteInterest(propertyId: String?) = viewModelScope.launch {
        repository.deleteInterest(propertyId)
    }
}