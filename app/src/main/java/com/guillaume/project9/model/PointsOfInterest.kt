package com.guillaume.project9.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points_interest_table")
data class PointsOfInterest(val propertyCreatorId: String?, val pointOfInterest: String?) {
    @PrimaryKey(autoGenerate = true)
    var pointOfInterestId: Int = 0
}