package com.example.pagingtest.db

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface ContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts : List<ContentEntity>)

    @Query("SELECT * FROM contents")
    fun getContents(): PagingSource<Int, ContentEntity>

    @Query("SELECT * FROM contents ORDER BY title")
    fun getContentsSortByTitle(): PagingSource<Int, ContentEntity>

    @Query("SELECT Count(*) FROM contents")
    fun getContentsSize(): Int?

    @Query("DELETE FROM contents")
    fun clearContets()
}