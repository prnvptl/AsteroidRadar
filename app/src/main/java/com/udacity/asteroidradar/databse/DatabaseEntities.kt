package com.udacity.asteroidradar.databse

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class DatabaseAsteroid constructor(
    @PrimaryKey val id: Long, val codename: String, val closeApproachDate: Date,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)

fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate =  SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(it.closeApproachDate),
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter =  it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}
