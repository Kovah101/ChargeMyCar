package com.github.kovah101.chargemycar.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

// Base URL for search query
private const val BASE_URL =
    "https://chargepoints.dft.gov.uk/api/retrieve/registry/"

// create Retrofit object
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

// create API service to request JSON response
// TODO: Need to manipulate URL here!
interface ChargePointAPIService {
    @GET("postcode/SW9/dist/10/limit/10/format/json")
    fun getChargeQuery():
            Call<String>
}

// public object to expose Retrofit service to the rest of the app
object ChargeApi {
    val retrofitService : ChargePointAPIService by lazy {
        retrofit.create(ChargePointAPIService::class.java)
    }
}