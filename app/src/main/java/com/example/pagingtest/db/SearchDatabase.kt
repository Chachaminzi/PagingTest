package com.example.pagingtest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [KeywordEntity::class], version = 1, exportSchema = false)
abstract class SearchDatabase : RoomDatabase() {
    abstract val keywordDao: KeywordDao
}

private lateinit var INSTANCE: SearchDatabase
fun getDatabase(context: Context): SearchDatabase {
    synchronized(SearchDatabase::class.java) {
        // lateinit 변수가 이미 할당되었는지 여부를 확인하는 if문
        if (!::INSTANCE.isInitialized) {
            // INSTANCE가 아직 초기화되지 않음
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                SearchDatabase::class.java,
                "search_database"
            ).build()
        }
    }
    return INSTANCE
}