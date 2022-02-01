package com.udacity.asteroidradar.databse

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {
    @Query("select * from databaseasteroid ORDER BY closeApproachDate DESC")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid WHERE closeApproachDate = :timeStamp ")
    fun getAsteroidsByDate(timeStamp: Long): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid where closeApproachDate BETWEEN :startTimeStamp AND :endTimeStamp ORDER BY closeApproachDate DESC")
    fun getAsteroidsStartEndRange(startTimeStamp: Long, endTimeStamp: Long): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)
}