package com.example.pagingtest.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.pagingtest.api.CafeSearchResponse
import com.example.pagingtest.api.KakaoService
import com.example.pagingtest.convertStringToDateString
import com.example.pagingtest.db.SearchDatabase
import com.example.pagingtest.models.Content
import com.example.pagingtest.paging.KakaoBlogPagingSource
import com.example.pagingtest.paging.KakaoCafePagingSource
import com.example.pagingtest.paging.KakaoRemoteMediator
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KakaoRepository(
    private val database: SearchDatabase,
    private val service: KakaoService
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getCafeResultStream(query: String) = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        ),
        remoteMediator = KakaoRemoteMediator(query, database, service)
    ) {
        database.contentDao.getContents()
    }.flow

    fun getBlogResultStream(query: String): Flow<PagingData<Content>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PAGE_SIZE,
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
    fun testCafe(query: String): Flow<CafeSearchResponse> {
        return flow {
            service.testCafe(
                query = query,
                page = 1,
                size = 25
            )
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 25
    }
}