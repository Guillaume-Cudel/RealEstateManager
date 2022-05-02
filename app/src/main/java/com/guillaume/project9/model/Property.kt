package com.guillaume.project9.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

//@Entity(foreignKeys = ForeignKey(entity = PropertyPhoto::class, parentColumns = "id", childColumns = "propertyPhotos"))


data class Property(@PrimaryKey val id: Int, val kind: String, val price: Int, val surface: Double, val roomsNumber: Int,
                    val description: String, val propertyPhotos: List<PropertyPhoto>, val address: String,
                    val pointOfInteret: List<String>, var statut: Boolean, val launchOrSellDate: Date,
                    val userInCharge: String)


