package com.guillaume.project9.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = arrayOf(ForeignKey(entity = PropertyPhoto::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("propertyPhotos"))),
    tableName = "property_table")


data class Property(val kind: String, val price: Int, val surface: Double, val rooms: Int?,
                    val description: String?, val propertyPhotos: List<PropertyPhoto?>, val address: String,
                    val postalCode: Int, val cityAddress: String, val pointOfInteret: MutableSet<String?>,
                    var selled: Boolean, val launchOrSellDate: String, val agent: String){
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
}


