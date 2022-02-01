package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.util.Constants
import com.udacity.asteroidradar.model.ImageOfTheDay
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.databse.AsteroidDatabase
import com.udacity.asteroidradar.databse.asDomainModel
import com.udacity.asteroidradar.util.getTimeStampStartOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

enum class AsteroidTimeFilter(val value: String) { WEEKLY("weekly"), TODAY("today"), ALL("all") }


class AsteroidRepository(private val database: AsteroidDatabase, private val apiService: AsteroidService)  {

    var todayTimeStamp: Long
    var seventhDayTimestamp: Long

    init {
        val sevenDays = getNextSevenDaysFormattedDates()
        todayTimeStamp = getTimeStampStartOfDay(sevenDays[0])
        seventhDayTimestamp  = getTimeStampStartOfDay(sevenDays[6])
    }


    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) { dbAsteroids ->
            dbAsteroids.asDomainModel()
    }

    val todaysAsteroids : LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidsByDate(getTimeStampStartOfDay(getNextSevenDaysFormattedDates()[0]))) { dbAsteroids ->
            dbAsteroids.asDomainModel()
        }

    val weeklyAsteroids : LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidsStartEndRange(
            todayTimeStamp,
            seventhDayTimestamp
        )) { dbAsteroids ->
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
            val asteroidJsonObj = apiService.getAsteroids(dateFormat.format(currentTime)).await()
            val asteroids = parseAsteroidsJsonResult(JSONObject(asteroidJsonObj))
            database.asteroidDao.insertAll(*NetworkAsteroidContainer(asteroids).asDatabaseModel())
        }
    }

    suspend fun getImageOfDayUrl(): ImageOfTheDay {
        val imageOfDayData = apiService.getImageOfDayData().await()
        return parseImageOfDayUrlFromData(JSONObject(imageOfDayData))
    }

}