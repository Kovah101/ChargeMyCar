package com.github.kovah101.chargemycar.savedDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "saved_charge_points_table")
data class ChargePoint(
    @PrimaryKey(autoGenerate = true)
    var chargePointId: Long = 0L,

    @ColumnInfo(name = "latitude")
    var latitude: Float = 0F,

    @ColumnInfo(name = "longitude")
    var longitude: Float = 0F,

    @ColumnInfo(name = "postcode")
    var postcode: String = "",

    @ColumnInfo(name = "connector_type")
    var connectorType: String = "",

    @ColumnInfo(name = "charge_point_status")
    var chargePointStatus: Boolean = false,

    @ColumnInfo(name = "location_type")
    var locationType: String = ""
)