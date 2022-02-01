package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.ImageOfTheDay
import com.udacity.asteroidradar.databse.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.AsteroidTimeFilter
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val apiService = Network.asteroidService
    private val asteroidsRepository = AsteroidRepository(database, apiService)

    private val _imageOfTheDay = MutableLiveData<ImageOfTheDay>()
    val imageOfTheDay: LiveData<ImageOfTheDay>
        get() = _imageOfTheDay

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid : LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    val isLoading = MutableLiveData<Boolean>(false)

    private val filterSelected = MutableLiveData<AsteroidTimeFilter>(AsteroidTimeFilter.ALL)

    val asteroids = Transformations.switchMap(filterSelected) { filter ->
        when (filter) {
            AsteroidTimeFilter.TODAY -> asteroidsRepository.todaysAsteroids
            AsteroidTimeFilter.WEEKLY -> asteroidsRepository.weeklyAsteroids
            else -> asteroidsRepository.asteroids
        }
    }

    init {
        viewModelScope.launch {
            try {
                isLoading.value = true
                _imageOfTheDay.value =  asteroidsRepository.getImageOfDayUrl()
                asteroidsRepository.refreshAsteroids()
            } catch (e: Exception) {
                Timber.e("SOME ERROR " + e.message)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun updateFilter(filter: AsteroidTimeFilter) {
        filterSelected.value = filter
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsDone() {
        _navigateToSelectedAsteroid.value = null
    }
}