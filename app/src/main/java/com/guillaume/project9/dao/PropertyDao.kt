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

    @Query("SELECT * FROM property_table ORDER BY launchOrSellDate DESC")
    fun getPropertysByLatestDate(): Flow<List<Property>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProperty(property: Property)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertPhotos(photos: List<Photo?>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertPointsOfInterest(pointOfInterest: List<PointsOfInterest?>)

    @Query("SELECT * FROM photo_table WHERE propertyCreatorId = :propertyId")
    fun getPropertyPhotosById(propertyId: String?): Flow<List<Photo>>

    @Query("SELECT * FROM points_interest_table WHERE propertyCreatorId = :propertyId")
    fun getPropertyPointsOfInterestById(propertyId: String?): Flow<List<PointsOfInterest>>

    @Transaction
    @Query("SELECT * FROM property_table")
    fun getPropertyPhotos(): List<PropertyWithPhotos>

    @Query("SELECT * FROM property_table WHERE kind = :kind AND price >= :priceMin AND price <= :priceMax AND surface >= :surfaceMin AND surface <= :surfaceMax ORDER BY launchOrSellDate DESC")
    fun searchPropertysWithConditions(kind: String, priceMin: Int, priceMax: Int,
                                      surfaceMin: Double, surfaceMax: Double): Flow<MutableList<Property>>


    @Update
    suspend fun updateProperty(property: Property)

    @Query("DELETE FROM photo_table WHERE propertyCreatorId = :propertyId")
    suspend fun deletePhotos(propertyId: String?)

    @Query("DELETE FROM points_interest_table WHERE propertyCreatorId = :propertyId")
    suspend fun deleteInterest(propertyId: String?)
}