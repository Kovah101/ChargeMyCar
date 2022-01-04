package com.github.kovah101.chargemycar.network

import com.github.kovah101.chargemycar.savedDatabase.ChargePoint
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

// Base URL for search query
private const val BASE_URL =
    "https://chargepoints.dft.gov.uk/api/retrieve/registry/"



// Build moshi object for retrofit to convert json to kotlin objects
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//private val jsonAdapter : JsonAdapter<List<ChargePoint>> = moshi.adapter<List<ChargePoint>>()

// create Retrofit object
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

// create API service to request JSON response
// returns a Coroutine [Object] of ChargeQuery that contains the list of Charge points which can be fetched with await() if in a Coroutine scope.
// TODO: Need to manipulate URL here!
interface ChargePointAPIService {

    @GET("postcode/SW9/dist/{distance}/limit/{limit}/format/json")
    suspend fun getChargeQueryObject(@Path("distance") distance: String, @Path("limit") limit: String): ChargeQuery


    @GET("postcode/SW9/dist/10/limit/10/format/json")
    fun getChargeQuery():
            Call<List<ChargePoint>>
}

// public object to expose Retrofit service to the rest of the app
object ChargeApi {
    val retrofitService : ChargePointAPIService by lazy {
        retrofit.create(ChargePointAPIService::class.java)
    }
}