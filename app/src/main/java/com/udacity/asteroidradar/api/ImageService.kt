package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.main.Image
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL="https://api.nasa.gov/"

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


private val retrofitImage= Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL).build()
interface ImageService {

    @GET("planetary/apod")
    suspend fun getImageOfTheDay(@Query("api_key")key: String): Image

}

//exposing to other class
object ImageApi{
    val retrofitService : ImageService by lazy {
        retrofitImage.create(ImageService::class.java)
    }
}
