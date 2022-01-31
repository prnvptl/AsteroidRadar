package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.ImageOfTheDay
import com.udacity.asteroidradar.databse.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidRepository(database)

    private val _imageOfTheDay = MutableLiveData<ImageOfTheDay>()
    val imageOfTheDay: LiveData<ImageOfTheDay>
        get() = _imageOfTheDay

    val asteroids = asteroidsRepository.asteroids


    init {
        viewModelScope.launch {
            _imageOfTheDay.value =  asteroidsRepository.getImageOfDayUrl()
            asteroidsRepository.refreshAsteroids()
        }
    }

}