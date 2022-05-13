package com.guillaume.project9.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File

@Entity(tableName = "photo_table")
data class Photo(val propertyCreatorId: String?, val photos: String?, val description: String?){
    @PrimaryKey(autoGenerate = true)
    var photoId: Int = 0
}