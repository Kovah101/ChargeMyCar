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
    @Json(name = "Connector") val connector : List<CPConnector>,
    @Json(name = "LocationType") var locationType : String = ""
        )

data class CPLocation (
    @Json(name = "Latitude") var latitude : String = "",
    @Json(name = "Longitude") var longitude : String = "",
    @Json(name = "Address") val address : CPAddress
        )

data class CPAddress (
    @Json(name = "PostCode") var postcode : String?
        )

data class CPConnector (
    @Json(name = "ConnectorType") var connectorType : String = "",
    @Json(name = "ChargePointStatus") var status : String = ""
        )