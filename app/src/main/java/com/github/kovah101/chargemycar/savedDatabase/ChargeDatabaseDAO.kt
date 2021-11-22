package com.github.kovah101.chargemycar.savedDatabase

import androidx.lifecycle.LiveData
import androidx.room.*

// interface to add/remove charge points from a saved list of favourites

@Dao
interface ChargeDatabaseDAO {
    // insert 1 specific charge point
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(point: ChargePoint)

    // insert a list of charge points
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(chargePoints: List<ChargePoint>)

    // updates the data for a charge point
    @Update
    fun update(point: ChargePoint)

    // get all saved charge points
    @Query("SELECT * FROM saved_charge_points_table ORDER BY chargePointId DESC")
    fun getAllPoints(): LiveData<List<ChargePoint>>

    // get a specific charge point
    // might have to change to key: PrimaryKey
    @Query("SELECT * FROM saved_charge_points_table WHERE chargePointId = :key")
    fun getPoint(key: Long): ChargePoint?

    // get a specific charge point
    // might have to change to key: PrimaryKey
    @Query("SELECT * FROM saved_charge_points_table WHERE postcode = :postcode")
    fun getPointByPostcode(postcode: String): ChargePoint?

    // delete specific charge point
    @Query("DELETE FROM saved_charge_points_table WHERE chargePointId = :key")
    fun removePoint(key: Long)

    // clear database
    @Query("DELETE FROM saved_charge_points_table")
    fun clear()

}