package com.guillaume.project9.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File

@Entity
data class PropertyPhoto(@PrimaryKey(autoGenerate = true) val id: String,
                         val photos: File,
                         val description: String?)