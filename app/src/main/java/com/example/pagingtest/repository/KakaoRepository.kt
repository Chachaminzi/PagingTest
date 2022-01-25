package com.example.pagingtest.repository

import androidx.paging.*
import com.example.pagingtest.api.KakaoService
import com.example.pagingtest.models.Content
import com.example.pagingtest.models.ItemModel
import com.example.pagingtest.paging.KakaoBlogPagingSource
import com.example.pagingtest.paging.KakaoCafePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class KakaoRepository @Inject constructor(
    private val service: KakaoService
) {

    fun getCafeResultStream(query: String, sortType: String): Flow<PagingData<Content>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { KakaoCafePagingSource(service, query, sortType) }
        ).flow.map { pagingData ->
            pagingData.map {
                Content(it)
            }
        }
    }

    fun getBlogResultStream(query: String, sortType: String): Flow<PagingData<Content>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { KakaoBlogPagingSource(service, query, sortType) }
        ).flow.map { pagingData ->
            pagingData.map {
                Content(it)
            }
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 25
    }
}