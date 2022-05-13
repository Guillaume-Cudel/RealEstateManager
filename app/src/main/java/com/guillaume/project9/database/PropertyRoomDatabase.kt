package com.guillaume.project9.database

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.guillaume.project9.dao.PropertyDao
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.Property
import com.guillaume.project9.model.PropertyTypeConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahChronology.INSTANCE

@Database(entities = [Property::class, Photo::class], version = 3)
@TypeConverters(PropertyTypeConverter::class)
abstract class PropertyRoomDatabase : RoomDatabase() {


    abstract fun propertyDao(): PropertyDao

    companion object {
        @Volatile
        private var INSTANCE: PropertyRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): PropertyRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, PropertyRoomDatabase::class.java,
                    "property_database"
                )
                    .addCallback(PropertyDatabaseCallback(scope))
                    //.fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


    private class PropertyDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {

                    val propertyDao = database.propertyDao()

                    val property = Property(
                        "first",
                        "House",
                        235000,
                        80.32,
                        4,
                        "House at 30min of toulouse center",
                        "4 rue virginia woolf",
                        31200,
                        "toulouse",
                        listOf("school", "Park", "shop"),
                        false,
                        "12/05/22 10:16",
                        "Vanessa Basset"
                    )

                    propertyDao.insertProperty(property)

                }
            }
        }
    }
}
