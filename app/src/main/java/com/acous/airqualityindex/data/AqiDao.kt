package com.acous.airqualityindex.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AqiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cityAqi: List<CityAqi>)


    @Query("SELECT * FROM cityAqi order by city ")
    fun getCitiesData(): LiveData<List<CityAqi>>

}