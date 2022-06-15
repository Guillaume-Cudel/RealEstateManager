package com.guillaume.project9.model

import androidx.room.*
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable
import java.util.*

@Entity(
    tableName = "property_table"
)


data class Property(
    @PrimaryKey val propertyId: String,
    val kind: String,
    val price: Int,
    val surface: Double,
    val rooms: Int?,
    val description: String?,
    var photo: String?,
    val address: String,
    val postalCode: Int,
    val cityAddress: String,
    var sold: Boolean,
    val launchOrSellDate: Long,
    val agent: String,
    val location: String?
) : Serializable


