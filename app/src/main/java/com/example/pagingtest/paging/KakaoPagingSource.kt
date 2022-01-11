package com.example.pagingtest.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pagingtest.api.CafeDocuments
import com.example.pagingtest.api.KakaoService
import com.example.pagingtest.repository.KakaoRepository.Companion.NETWORK_PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException

private const val KAKAO_STARTING_PAGE_INDEX = 1

class KakaoPagingSource(
    private val service: KakaoService,
    private val query: String
) : PagingSource<Int, CafeDocuments>() {

    override fun getRefreshKey(state: PagingState<Int, CafeDocuments>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CafeDocuments> {
        val position = params.key ?: KAKAO_STARTING_PAGE_INDEX
        val apiQuery = query

        return try {
            val response = service.searchCafe()
            val documents = response.documents
            val nextKey = if (documents.isEmpty()) {
                null
            } else {
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = documents
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}