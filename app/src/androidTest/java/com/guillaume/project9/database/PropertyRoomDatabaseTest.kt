package com.guillaume.project9.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guillaume.project9.dao.PropertyDao
import com.guillaume.project9.model.Photo
import com.guillaume.project9.model.Property
import junit.framework.TestCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PropertyRoomDatabaseTest: TestCase() {

    var property = Property("First", "House", 180000, 45.22, 2, "Little house",
        null, "4 rue voltaire", 31000, "Toulouse", false, 1655282245921,
        "Vanessa Basset", null)

    private lateinit var propertyDao: PropertyDao
    private lateinit var database: PropertyRoomDatabase

    @Before
    public override fun setUp(){

        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PropertyRoomDatabase::class.java).build()
        propertyDao = database.propertyDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }


    @Test
    fun addAndUpdateProperty(): Unit = runBlocking{


        propertyDao.insertProperty(property)
        val flowList = propertyDao.getPropertysByLatestDate()
        flowList.map {
        assertTrue(it[0].propertyId == property.propertyId)
        }

        property = Property("First", "Ground", 180000, 45.22, 2, "Little house",
            null, "4 rue voltaire", 31000, "Toulouse", false, 1655282245921,
            "Vanessa Basset", null)
        propertyDao.updateProperty(property)
        val newFlowList = propertyDao.getPropertysByLatestDate()
        newFlowList.map {
            assertTrue(it[0].kind == property.kind)
        }
    }

    @Test
    fun addAndDeletePhoto(): Unit = runBlocking {
        val photo = Photo(property.propertyId, "Nothing", "Living room")
        val photoList: List<Photo> = listOf(photo)
        propertyDao.insertProperty(property)
        propertyDao.insertPhotos(photoList)

        val flowList = propertyDao.getPropertysByLatestDate()
        flowList.map {
            val flowPhotoListRecoved = propertyDao.getPropertyPhotosById(it[0].propertyId)
            flowPhotoListRecoved.map { photos ->
                assertTrue(photos.isNotEmpty() && it[0].propertyId == photos[0].propertyCreatorId)

                propertyDao.deletePhotos(it[0].propertyId)
                assertTrue(photos.isEmpty())
            }
        }
    }


}