package com.example.pagingtest.di

import android.content.Context
import androidx.room.Room
import com.example.pagingtest.db.KeywordDao
import com.example.pagingtest.db.SearchDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): SearchDatabase {
        return Room.databaseBuilder(
            appContext,
            SearchDatabase::class.java,
            "search_database"
        ).build()
    }

    @Provides
    fun provideKeywordDao(database: SearchDatabase): KeywordDao {
        return database.keywordDao
    }
}