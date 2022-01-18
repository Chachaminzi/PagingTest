package com.example.pagingtest.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface KeywordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keyword: KeywordEntity)

    @Query("DELETE FROM keywords where record_time < (SELECT min(record_time) from (SELECT * FROM keywords ORDER BY record_time DESC limit 5))")
    suspend fun deleteLast()

    @Query("SELECT * FROM keywords")
    fun getAllKeyword(): LiveData<List<KeywordEntity>>

    @Query("SELECT * FROM keywords")
    fun testAllKeyword(): List<KeywordEntity>
}