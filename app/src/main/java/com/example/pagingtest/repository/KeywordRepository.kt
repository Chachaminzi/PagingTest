package com.example.pagingtest.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.pagingtest.db.KeywordEntity
import com.example.pagingtest.db.SearchDatabase
import com.example.pagingtest.db.asDomainModel
import javax.inject.Inject

class KeywordRepository @Inject constructor(private val database: SearchDatabase) {
    val keywords: LiveData<List<String>> =
        Transformations.map(database.keywordDao.getAllKeyword()) {
            it.asDomainModel()
        }

    suspend fun updateKeyword(keyword: KeywordEntity) {
        database.keywordDao.insert(keyword)
    }

    suspend fun deleteKeywordOverLimit() {
        database.keywordDao.deleteLast()
    }
}