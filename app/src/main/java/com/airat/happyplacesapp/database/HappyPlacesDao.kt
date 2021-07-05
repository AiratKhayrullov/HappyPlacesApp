package com.airat.happyplacesapp.database

import androidx.room.*
import com.airat.happyplacesapp.models.HappyPlaceModel

@Dao
interface HappyPlacesDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHappyPlace(happyPlace : HappyPlaceModel)


    @Transaction
    @Query("SELECT * FROM happyplacemodel WHERE id = :idModel")
    suspend fun getHappyPlace(idModel : Int) : HappyPlaceModel



    @Transaction
    @Query("SELECT * FROM happyplacemodel")
    suspend fun getAllHappyPlaces() : List<HappyPlaceModel>




    @Transaction
    @Update
    suspend fun updateHappyPlace(happyPlaceModel: HappyPlaceModel)

    @Transaction
    @Delete
    suspend fun deleteHappyPlace(happyPlace: HappyPlaceModel)

}