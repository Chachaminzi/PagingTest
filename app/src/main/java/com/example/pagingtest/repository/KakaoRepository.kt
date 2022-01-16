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

    private fun insertDateSeparator(it: PagingData<Content>): PagingData<Content> {
        return it.insertSeparators { before, after ->
            if (after == null) return@insertSeparators null
            if (before == null) return@insertSeparators null

            val beforeDate = before.dateTime
            val afterDate = after.dateTime

            Log.d("cafe before : ", before.toString())
            Log.d("cafe after: ", before.toString())
            if (beforeDate > afterDate) {
                before.thumbnail = after.thumbnail.also { after.thumbnail = before.thumbnail }
                before.label = after.label.also { after.label = before.label }
                before.name = after.name.also { after.name = before.name }
                before.title = after.title.also { after.title = before.title }
                before.contents = after.contents.also { after.contents = before.contents }
                before.dateTime = after.dateTime.also { after.dateTime = before.dateTime }
                before.url = after.url.also { after.url = before.url }
            }

            return@insertSeparators null
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