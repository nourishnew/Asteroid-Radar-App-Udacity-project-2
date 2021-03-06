package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL="https://api.nasa.gov/"

private val retrofit= Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL).build()

interface NasaApiService {
    @GET("neo/rest/v1/feed")
  suspend  fun getAsteroidsAsync(@Query("api_key") key:String, @Query("start_date") startDate:String, @Query("end_date") endDate:String): String

}

//exposing to other class
object NasaApi{
    val retrofitService : NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}
