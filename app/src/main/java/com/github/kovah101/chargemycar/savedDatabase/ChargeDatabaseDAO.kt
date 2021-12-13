package com.github.kovah101.chargemycar.savedDatabase

import androidx.lifecycle.LiveData
import androidx.room.*

// interface to add/remove charge points from a saved list of favourites

@Dao
interface ChargeDatabaseDAO {
    // insert 1 specific charge point
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(point: ChargePoint)

    // insert a list of charge points
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chargePoints: List<ChargePoint>)

    // updates the data for a charge point
    @Update
    suspend fun update(point: ChargePoint)

    // get all saved charge points
    @Query("SELECT * FROM saved_charge_points_table ORDER BY chargePointId DESC")
    fun getAllPoints(): LiveData<List<ChargePoint>>

    // get a specific charge point
    // might have to change to key: PrimaryKey
    @Query("SELECT * FROM saved_charge_points_table WHERE chargePointId = :key")
    suspend fun getPoint(key: Long): ChargePoint?

    // get a specific charge point
    // might have to change to key: PrimaryKey
    @Query("SELECT * FROM saved_charge_points_table WHERE postcode = :postcode")
    suspend fun getPointByPostcode(postcode: String): ChargePoint?

    // get a specific charge point
    // using latitude and longitude
    @Query("SELECT * FROM saved_charge_points_table WHERE latitude = :latitude AND longitude = :longitude")
    suspend fun getPointByLatAndLong(latitude: Float, longitude: Float) : ChargePoint?

    // delete specific charge point
    @Query("DELETE FROM saved_charge_points_table WHERE chargePointId = :key")
    suspend fun removePoint(key: Long)

    // clear database
    @Query("DELETE FROM saved_charge_points_table")
    suspend fun clear()

}