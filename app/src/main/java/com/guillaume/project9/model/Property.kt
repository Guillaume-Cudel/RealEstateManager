package com.guillaume.project9.model

import java.util.*

data class Property(val kind: String, val price: Int, val surface: Double, val roomsNumber: Int,
                    val description: String, val propertyPhoto: PropertyPhoto, val address: String,
                    val pointOfInteret: List<String>, var statut: Boolean, val launchDate: Date,
                    val sellDate: Date?, val userInCharge: String)