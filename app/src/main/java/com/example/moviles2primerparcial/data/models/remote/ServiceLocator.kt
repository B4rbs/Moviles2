package com.example.moviles2primerparcial.data.models.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Simple DI-less service locator for small apps.
 * Exposes a Repository as `api` with a method getBreeds().
 */
object ServiceLocator {

    private const val BASE_URL = "https://api.thecatapi.com/v1/"

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private val okHttp: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private val catApi: CatApi by lazy {
        retrofit.create(CatApi::class.java)
    }

    /** Exposed entry point from UI layer */
    val api: Repository by lazy { Repository(catApi) }
}


