package com.guillaume.project9.model

import androidx.room.*
import java.util.*

@Entity(/*foreignKeys = [ForeignKey(entity = PropertyPhoto::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("propertyPhotos"),
    onUpdate = ForeignKey.CASCADE)],*/
    tableName = "property_table")


data class Property(@PrimaryKey val propertyId: String, val kind: String, val price: Int, val surface: Double, val rooms: Int?,
                    val description: String?, @TypeConverters(PropertyTypeConverter::class) var photos: List<String?>,
                    val address: String, val postalCode: Int, val cityAddress: String,
                    @TypeConverters(PropertyTypeConverter::class) val pointOfInterest: List<String?>,
                    var selled: Boolean, val launchOrSellDate: String, val agent: String)


