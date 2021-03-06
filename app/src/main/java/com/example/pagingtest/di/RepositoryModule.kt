package com.example.pagingtest.di

import com.example.pagingtest.api.KakaoService
import com.example.pagingtest.db.SearchDatabase
import com.example.pagingtest.repository.KakaoRepository
import com.example.pagingtest.repository.KeywordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityComponent::class)
@Module
class RepositoryModule {

    @Provides
    fun provideKakaoRepository(
        service: KakaoService
    ): KakaoRepository {
        return KakaoRepository(service)
    }

    @Provides
    fun provideKeywordRepository(
        database: SearchDatabase
    ): KeywordRepository {
        return KeywordRepository(database)
    }
}