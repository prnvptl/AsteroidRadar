package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.ImageOfTheDay
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.databse.AsteroidDatabase
import com.udacity.asteroidradar.databse.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase)  {
    /**
     * A list of asteroid that can be shown on the screen.
     */
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) { dbAsteroids ->
            dbAsteroids.asDomainModel()
    }

    /**
     * Refresh the asteroids stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     * To actually load the asteroids for use, observe [asteroids]
     */
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val calendar = Calendar.getInstance()
            val currentTime = calendar.time
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            val asteroidJsonObj = Network.asteroidService.getAsteroids(dateFormat.format(currentTime)).await()
            val asteroids = parseAsteroidsJsonResult(JSONObject(asteroidJsonObj))
            database.asteroidDao.insertAll(*NetworkAsteroidContainer(asteroids).asDatabaseModel())
        }
    }

    suspend fun getImageOfDayUrl(): ImageOfTheDay {
        val imageOfDayData = Network.asteroidService.getImageOfDayData().await()
        return parseImageOfDayUrlFromData(JSONObject(imageOfDayData))
    }

}