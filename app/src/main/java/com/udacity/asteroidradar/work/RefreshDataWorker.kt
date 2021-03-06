package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.databse.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException
import timber.log.Timber

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "AsteroidRefreshDataWorker"
    }

    /**
     * A coroutine-friendly method to do your work.
     */
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val apiService = Network.asteroidService
        val repository = AsteroidRepository(database, apiService)
        return try {
            repository.refreshAsteroids()
            return Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}
