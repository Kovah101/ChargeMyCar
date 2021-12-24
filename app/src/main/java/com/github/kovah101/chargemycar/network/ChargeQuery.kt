package com.github.kovah101.chargemycar.network

import com.github.kovah101.chargemycar.savedDatabase.ChargePoint
import com.squareup.moshi.Json

data class ChargeQuery (
 @Json(name = "Scheme") val scheme : Scheme,
 @Json(name = "ChargeDevice") val chargeDevices : List<ChargePoint>
)

data class Scheme (
 val SchemeCode: String
        )
