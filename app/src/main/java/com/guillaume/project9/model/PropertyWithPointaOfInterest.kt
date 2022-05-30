package com.guillaume.project9.model

import androidx.room.Embedded
import androidx.room.Relation

class PropertyWithPointaOfInterest(
    @Embedded val property: Property,
    @Relation(
        parentColumn = "propertyId",
        entityColumn = "propertyCreatorId"
    )
    val pointsOfInterest: List<PointsOfInterest>)

class PointsOfInterestWithProperty(
    @Embedded val pointOfInterest: PointsOfInterest,
    @Relation(
        parentColumn = "pointOfInterest",
        entityColumn = "propertyId"
    )
    val propertys: List<Property>)