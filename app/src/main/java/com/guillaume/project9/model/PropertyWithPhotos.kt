package com.guillaume.project9.model

import androidx.room.Embedded
import androidx.room.Relation

class PropertyWithPhotos(
    @Embedded val property: Property,
    @Relation(
        parentColumn = "propertyId",
        entityColumn = "propertyCreatorId"
    )
    val photos: List<Photo>)