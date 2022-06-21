package com.guillaume.project9.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guillaume.project9.model.Property

class UtilsViewModel: ViewModel() {

    val data = MutableLiveData<Property>()

    fun saveProperty(property: Property){
        data.value = property
    }
}