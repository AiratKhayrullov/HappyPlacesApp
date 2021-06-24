package com.airat.happyplacesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.airat.happyplacesapp.models.HappyPlaceModel

@Database(
    entities= [
        HappyPlaceModel::class
    ],
    version = 1
)
abstract class HappyPlacesDatabase : RoomDatabase(){
    abstract val happyPlaceDao: HappyPlacesDao

    companion object{
        @Volatile
        private var INSTANCE: HappyPlacesDatabase? = null

        fun getInstance(context: Context): HappyPlacesDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    HappyPlacesDatabase::class.java,
                    "happyPlaces_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}