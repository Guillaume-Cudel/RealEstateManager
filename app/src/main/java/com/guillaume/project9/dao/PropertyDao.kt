package com.guillaume.project9.dao

import androidx.room.*
import com.guillaume.project9.model.Property

@Dao
interface PropertyDao {

    @Query("SELECT * FROM property_table ORDER BY launchOrSellDate DESC")
    fun getPropertysByLatestDate(): List<Property>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(property: Property)

    @Query("SELECT * FROM property_table")
    fun getAllProperty(): List<Property>

    @Update
    fun updateProperty(property: Property)
}