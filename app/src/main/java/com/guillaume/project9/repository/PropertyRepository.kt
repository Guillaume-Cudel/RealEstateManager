package com.guillaume.project9.repository

import androidx.annotation.WorkerThread
import com.guillaume.project9.dao.PropertyDao
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.Property
import kotlinx.coroutines.flow.Flow

class PropertyRepository(private val propertyDao: PropertyDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allPropertys: Flow<List<Property>> = propertyDao.getPropertysByLatestDate()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertProperty(property: Property) {
        propertyDao.insertProperty(property)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertPhoto(photo: Photo) {
        propertyDao.insertPhoto(photo)
    }
}