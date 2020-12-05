package com.udacity.asteroidradar.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Database.AsteroidDatabase
import com.udacity.asteroidradar.Database.DatabaseAsteroid
import com.udacity.asteroidradar.Database.asDomainModel
import com.udacity.asteroidradar.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class AsteroidRepository(private val database: AsteroidDatabase) {

    var asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAllAsteroids()){
        it.asDomainModel()
    }

    suspend fun refreshAsteroids(){
        var asteroidArray= listOf<DatabaseAsteroid>()

        withContext(Dispatchers.IO){
            val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
            try {
                val json=NasaApi.retrofitService.getAsteroidsAsync(Constants.API_KEY,nextSevenDaysFormattedDates[0],nextSevenDaysFormattedDates[6])
                val obj = JSONObject(json)
                val asteroidList= parseAsteroidsJsonResult(obj)
                Log.e("asteroidList",""+asteroidList.size)
                // tried printing asteroidlist[0]. this is printing crctly
                val databaseAsteroid= asteroidList.map {
                    DatabaseAsteroid(
                            id = it.id,
                            codename = it.codename,
                            closeApproachDate = it.closeApproachDate,
                            absoluteMagnitude = it.absoluteMagnitude,
                            estimatedDiameter=it.estimatedDiameter,
                            relativeVelocity = it.relativeVelocity,
                            distanceFromEarth = it.distanceFromEarth,
                            isPotentiallyHazardous = it.isPotentiallyHazardous
                    )
                }
                asteroidArray=databaseAsteroid
                var final=asteroidArray.toTypedArray()
                Log.e("closedate",asteroidArray[0].closeApproachDate)
                // this is printing null.
                database.asteroidDao.insertAll(*final)
                //successful
            } catch (t: Throwable) {
                //failed
            }

        }
    }
}





