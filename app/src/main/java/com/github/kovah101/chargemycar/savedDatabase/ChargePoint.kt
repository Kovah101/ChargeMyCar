package com.github.kovah101.chargemycar.savedDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


@Entity(tableName = "saved_charge_points_table")
data class ChargePoint(
    @PrimaryKey(autoGenerate = true)
    var chargePointId: Long = 0L,

    @ColumnInfo(name = "latitude")
    @Json(name = "Latitude") var latitude: String = "",

    @ColumnInfo(name = "longitude")
    @Json(name = "Longitude") var longitude: String = "",

    @ColumnInfo(name = "postcode")
    @Json(name = "Postcode") var postcode: String = "",

    @ColumnInfo(name = "connector_type")
    @Json(name = "ConnectorType") var connectorType: String = "",

    @ColumnInfo(name = "charge_point_status")
    @Json(name = "ChargePointStatus") var chargePointStatus: String = "",

    @ColumnInfo(name = "location_type")
    @Json(name = "LocationType") var locationType: String = ""
)