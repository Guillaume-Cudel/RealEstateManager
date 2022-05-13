package com.guillaume.project9.di

import android.app.Application
import com.guillaume.project9.database.PropertyRoomDatabase
import com.guillaume.project9.repository.PropertyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PropertysApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { PropertyRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { PropertyRepository(database.propertyDao()) }
}