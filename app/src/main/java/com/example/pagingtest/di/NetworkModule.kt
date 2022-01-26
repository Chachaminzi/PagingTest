package com.example.pagingtest.di

import android.util.Log
import com.example.pagingtest.api.KakaoService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor {
            Log.d("API", it)
        }.apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KakaoBaseUrl

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NaverBaseUrl


    @KakaoBaseUrl
    @Provides
    fun provideKakaoBaseUrl(): String {
        return "https://dapi.kakao.com"
    }

    @NaverBaseUrl
    @Provides
    fun provideNaverBaseUrl(): String {
        return "https://openapi.naver.com"
    }

    @Provides
    fun provideKakaoRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
        @KakaoBaseUrl baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoService(retrofit: Retrofit): KakaoService {
        return retrofit.create(KakaoService::class.java)
    }
}