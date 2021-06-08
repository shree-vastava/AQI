package com.acous.airqualityindex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CityAqi::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun aqiDao() : AqiDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance
                ?: synchronized(this) { instance
                    ?: buildDatabase(
                        context
                    )
                        .also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "AQI_DB")
                .fallbackToDestructiveMigration()
                .build()
    }

}