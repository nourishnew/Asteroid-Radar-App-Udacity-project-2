package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Database.getDatabase
import com.udacity.asteroidradar.Repository.AsteroidRepository
import com.udacity.asteroidradar.api.ImageApi
import kotlinx.coroutines.launch
enum class ImageApiStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    private val _status = MutableLiveData<ImageApiStatus>()
    val status: LiveData<ImageApiStatus>
        get() = _status

    private val _image = MutableLiveData<Image>()
    val image: LiveData<Image>
        get() = _image


    private val database= getDatabase(application)
    private val asteroidRepository= AsteroidRepository(database)
    val asteroids= asteroidRepository.asteroids

    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
        getImageOfTheDay()
    }

    fun displayPropertyDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }
      private fun getImageOfTheDay() {
        viewModelScope.launch {
            _status.value = ImageApiStatus.LOADING
            try {
                var result = ImageApi.retrofitService.getImageOfTheDay(Constants.API_KEY)
                _image.value=result
                _status.value = ImageApiStatus.DONE
            } catch (e: Exception) {
                _status.value=ImageApiStatus.ERROR
                _image.value=Image("","","","","","","","")
            }
        }
    }


}