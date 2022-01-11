package com.example.pagingtest.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoService {

    @GET("/v2/search/blog")
    fun searchBlog(
        @Header("Authorization") apiKey: String = API_KEY,
        @Query("query") query: String,
        @Query("sort") sort: String = "accuracy",
        @Query("page") page: Int,
        @Query("size") size: Int
    )

    @GET("/v2/search/cafe")
    fun searchCafe(
        @Header("Authorization") apiKey: String = API_KEY,
        @Query("query") query: String,
        @Query("sort") sort: String = "accuracy",
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : CafeSearchResponse


    companion object {
        private const val BASE_URL = "https://dapi.kakao.com"
        private const val API_KEY = "cf985255b7b7b4352176956958b45b03"
    }

    object Network {
        private val logger = HttpLoggingInterceptor {
            Log.d("API", it)
        }.apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        private val client = OkHttpClient.Builder().addInterceptor(logger).build()
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(KakaoService::class.java)
    }

}