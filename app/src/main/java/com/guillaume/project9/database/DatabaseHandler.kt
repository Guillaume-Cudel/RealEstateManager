package com.guillaume.project9.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Property_database"
        private const val TABLE_PROPERTY = "PropertyTable"

        private const val KEY_ID = "_id"
        private const val KEY_KIND = "kind"
        private const val KEY_PRICE = "price"
        private const val KEY_SURFACE = "surface"
        private const val KEY_ROOMS = "rooms"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_PHOTOS = "photos"
        private const val KEY_ADDRESS = "address"
        private const val KEY_POSTAL_CODE = "postal_code"
        private const val KEY_CITY = "city"
        private const val KEY_INTEREST_POINTS = "interest_points"
        // STATUT = in sell or selled
        private const val KEY_STATUT = "statut"
        // DATE = launch date or sell date
        private const val KEY_DATE = "date"
        private const val KEY_AGENT = "agent"
    }

    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}