package com.udacity.asteroidradar.Database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DatabaseDao{

    @Query("SELECT * FROM databaseasteroid")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertAll(vararg asteroid: DatabaseAsteroid)



}

@Database(entities = [DatabaseAsteroid::class],version = 1)
abstract class AsteroidDatabase: RoomDatabase(){
    abstract val asteroidDao: DatabaseDao

}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase{
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids").build()
        }
    }
    return INSTANCE
}