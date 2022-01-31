package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val API_KEY = "fZ8QfFoRGY73Sda2ZyxrCydaIf7AsRKAZEEjQgps"

interface AsteroidService {

    @GET("neo/rest/v1/feed")
    fun getAsteroids(
        @Query("start_date") statDate: String,
        @Query("api_key") apiKey: String = API_KEY
    ) : Deferred<String>

    @GET("planetary/apod")
    fun getImageOfDayData(
        @Query("api_key") apiKey: String = API_KEY
    ) : Deferred<String>
}


/**
 * Main entry point for network access. Call like `Network.asteroidService.getAsteroids()`
 */
object Network {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val asteroidService = retrofit.create(AsteroidService::class.java)
}