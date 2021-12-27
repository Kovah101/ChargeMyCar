package com.github.kovah101.chargemycar.network

import com.github.kovah101.chargemycar.savedDatabase.ChargePoint
import com.squareup.moshi.Json

data class ChargeQuery (
 @Json(name = "Scheme") val scheme : Scheme,
 @Json(name = "ChargeDevice") val chargeDevices : List<ChargeQueryPoint>
)

data class Scheme (
 val SchemeCode: String
        )

data class ChargeQueryPoint (
    @Json(name = "ChargeDeviceLocation") val location : CPLocation,
    @Json(name = "Connector") val connector : CPConnector,
    @Json(name = "LocationType") val locationType : String
        )

data class CPLocation (
    @Json(name = "Latitude") val latitude : String,
    @Json(name = "Longitude") val longitude : String,
    @Json(name = "Address") val address : CPAddress
        )

data class CPAddress (
    @Json(name = "Postcode") val postcode : String
        )

data class CPConnector (
    @Json(name = "ConnectorType") val connectorType : String,
    @Json(name = "ChargePointStatus") val status : String
        )