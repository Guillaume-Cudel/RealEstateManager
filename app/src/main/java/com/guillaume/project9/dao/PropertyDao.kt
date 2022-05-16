package com.guillaume.project9.dao

import androidx.room.*
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.Property
import com.guillaume.project9.model.PropertyWithPhotos
import kotlinx.coroutines.flow.Flow
import kotlin.collections.List as List

@Dao
interface PropertyDao {

    @Query("SELECT * FROM property_table ORDER BY launchOrSellDate DESC")
    fun getPropertysByLatestDate(): Flow<List<Property>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProperty(property: Property)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertPhotos(photos: List<Photo?>)

    @Query("SELECT * FROM property_table")
    fun getAllProperty(): List<Property>

    //todo check getPropertyByLatestDate() to get photos
    @Query("SELECT * FROM photo_table WHERE propertyCreatorId = :propertyId")
    fun getPropertyPhotosById(propertyId: String?): Flow<List<Photo>>

    @Update
    fun updateProperty(property: Property)

    @Transaction
    @Query("SELECT * FROM property_table")
    fun getPropertyPhotos(): List<PropertyWithPhotos>
}