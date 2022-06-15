package com.guillaume.project9.repository

import androidx.annotation.WorkerThread
import com.guillaume.project9.dao.PropertyDao
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.PointsOfInterest
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
    suspend fun insertPhotos(photos: List<Photo?>) {
        propertyDao.insertPhotos(photos)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertPointOfInterest(pointsOfInterest: List<PointsOfInterest?>){
        propertyDao.insertPointsOfInterest(pointsOfInterest)
    }

    @WorkerThread
    fun getPhotosByProperty(propertyId: String?):Flow<List<Photo>> {
        return propertyDao.getPropertyPhotosById(propertyId)
    }

    @WorkerThread
    fun getPointsOfInterest(propertyId: String?): Flow<List<PointsOfInterest>>{
        return propertyDao.getPropertyPointsOfInterestById(propertyId)
    }

    @WorkerThread
    suspend fun updateProperty(property: Property){
        propertyDao.updateProperty(property)
    }

    @WorkerThread
    suspend fun deletePhotos(propertyId: String?){
        propertyDao.deletePhotos(propertyId)
    }

    @WorkerThread
    suspend fun deleteInterest(propertyId: String?){
        propertyDao.deleteInterest(propertyId)
    }
}