package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.BuildConfig
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidService {
    @GET("neo/rest/v1/feed")
    fun getAsteroids(
        @Query("start_date") statDate: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ) : Deferred<String>

    @GET("planetary/apod")
    fun getImageOfDayData(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ) : Deferred<String>
}