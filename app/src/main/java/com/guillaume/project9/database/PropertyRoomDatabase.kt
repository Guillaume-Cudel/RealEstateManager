package com.guillaume.project9.database

import android.content.Context
import android.os.Environment
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.guillaume.project9.dao.PropertyDao
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.Property
import com.guillaume.project9.model.PropertyTypeConverter
import java.io.File

@Database(entities = [Property::class, Photo::class], version = 2)
@TypeConverters(PropertyTypeConverter::class)
abstract class PropertyRoomDatabase: RoomDatabase() {


    abstract fun propertyDao(): PropertyDao



    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PropertyRoomDatabase? = null

        fun getDatabase(context: Context): PropertyRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, PropertyRoomDatabase::class.java,
                    "property_database")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
