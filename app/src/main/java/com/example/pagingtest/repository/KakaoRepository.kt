package com.example.pagingtest.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pagingtest.api.CafeDocuments
import com.example.pagingtest.api.KakaoService
import com.example.pagingtest.paging.KakaoPagingSource
import kotlinx.coroutines.flow.Flow

class KakaoRepository(private val service: KakaoService) {

    fun getCafeResultStream(query: String): Flow<PagingData<CafeDocuments>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { KakaoPagingSource(service, query) }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 25
    }
}