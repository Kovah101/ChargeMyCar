package com.github.kovah101.chargemycar.savedDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChargePoint::class], version = 1, exportSchema = false)
abstract class ChargeDatabase : RoomDatabase(){

    // reference to Data Access Object
    abstract val chargeDatabaseDAO: ChargeDatabaseDAO

    companion object {

        @Volatile
        private var INSTANCE: ChargeDatabase? = null

        fun getInstance(context:Context): ChargeDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ChargeDatabase::class.java,
                        "saved_charge_point_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}