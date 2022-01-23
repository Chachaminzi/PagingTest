package com.example.pagingtest.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KeywordEntity::class], version = 1, exportSchema = false)
abstract class SearchDatabase : RoomDatabase() {
    abstract val keywordDao: KeywordDao
}