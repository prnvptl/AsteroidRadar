package com.udacity.asteroidradar.api

import kotlinx.coroutines.Deferred
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