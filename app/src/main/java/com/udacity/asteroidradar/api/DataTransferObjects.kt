package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.databse.DatabaseAsteroid
import java.text.SimpleDateFormat
import java.util.*

data class NetworkAsteroidContainer(val asteroids: List<Asteroid>)

/**
 * Converts Network object to database object
 */
fun NetworkAsteroidContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    return asteroids.map {
        DatabaseAsteroid(
            id =  it.id,
            codename = it.codename,
            closeApproachDate = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).parse(it.closeApproachDate),
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}
