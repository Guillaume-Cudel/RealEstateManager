package com.guillaume.project9.dao

import androidx.room.*
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.PointsOfInterest
import com.guillaume.project9.model.Property
import com.guillaume.project9.model.PropertyWithPhotos
import kotlinx.coroutines.flow.Flow
import kotlin.collections.List as List

@Dao
interface PropertyDao {

    @Query("SELECT * FROM property_table ORDER BY launchOrSellDate ASC")
    fun getPropertysByLatestDate(): Flow<List<Property>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProperty(property: Property)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertPhotos(photos: List<Photo?>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertPointsOfInterest(pointOfInterest: List<PointsOfInterest?>)

    @Query("SELECT * FROM property_table")
    fun getAllProperty(): List<Property>

    @Query("SELECT * FROM photo_table WHERE propertyCreatorId = :propertyId")
    fun getPropertyPhotosById(propertyId: String?): Flow<List<Photo>>

    @Query("SELECT * FROM points_interest_table WHERE propertyCreatorId = :propertyId")
    fun getPropertyPointsOfInterestById(propertyId: String?): Flow<List<PointsOfInterest>>

    @Transaction
    @Query("SELECT * FROM property_table")
    fun getPropertyPhotos(): List<PropertyWithPhotos>

    @Update
    suspend fun updateProperty(property: Property)

    @Query("DELETE FROM photo_table WHERE propertyCreatorId = :propertyId")
    suspend fun deletePhotos(propertyId: String?)

    @Query("DELETE FROM points_interest_table WHERE propertyCreatorId = :propertyId")
    suspend fun deleteInterest(propertyId: String?)
}