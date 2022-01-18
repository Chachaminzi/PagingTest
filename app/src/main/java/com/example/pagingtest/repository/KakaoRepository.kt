package com.example.pagingtest.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.pagingtest.api.CafeSearchResponse
import com.example.pagingtest.api.KakaoService
import com.example.pagingtest.convertStringToDateString
import com.example.pagingtest.models.Content
import com.example.pagingtest.paging.KakaoBlogPagingSource
import com.example.pagingtest.paging.KakaoCafePagingSource
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KakaoRepository(private val service: KakaoService) {

    fun getCafeResultStream(query: String): Flow<PagingData<Content>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { KakaoCafePagingSource(service, query) }
        ).flow.map { pagingData ->
            pagingData.map {
                Content(it)
            }
        }
    }

    fun getBlogResultStream(query: String): Flow<PagingData<Content>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { KakaoBlogPagingSource(service, query) }
        ).flow.map { pagingData ->
            pagingData.map {
                Content(it)
            }
        }
    }

    /**
     * test
     **/
    suspend fun testCafe(query: String): Int {
        return service.testCafe(
            query = query,
            page = 1,
            size = 1
        ).count()
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 25
    }
}