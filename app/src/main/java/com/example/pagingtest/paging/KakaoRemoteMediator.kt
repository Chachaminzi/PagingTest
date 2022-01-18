package com.example.pagingtest.paging

import androidx.paging.*
import androidx.room.withTransaction
import com.example.pagingtest.api.KakaoService
import com.example.pagingtest.db.ContentEntity
import com.example.pagingtest.db.RemoteKeys
import com.example.pagingtest.db.SearchDatabase
import retrofit2.HttpException
import java.io.IOException

private const val KAKAO_STARTING_PAGE_INDEX = 1
private const val PAGE_SIZE = 25

@ExperimentalPagingApi
class KakaoRemoteMediator(
    private val apiQuery: String,
    private val database: SearchDatabase,
    private val service: KakaoService
) : RemoteMediator<Int, ContentEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ContentEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: KAKAO_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = (remoteKeys != null))
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = (remoteKeys != null))
                nextKey
            }
        }

        try {
            val apiResponse = service.searchCafe(query = apiQuery, page = page, size = PAGE_SIZE)

            val cafes = apiResponse.documents.map {
                ContentEntity(
                    thumbnail = it.thumbnail,
                    label = "cafe",
                    name = it.cafeName,
                    title = it.title,
                    contents = it.contents,
                    dateTime = it.datetime,
                    url = it.url,
                    isClicked = false
                )
            }
            val endOfPaginationReached = cafes.isNullOrEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeyDao.clearRemoteKeys()
                    database.contentDao.clearContets()
                }

                database.contentDao.insertAll(cafes)

                val cafeLastId = database.contentDao.getContentsSize() ?: 0

                val prevKey = if (page == KAKAO_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                var diff = 0
                val keys = cafes.map {
                    ++diff
                    RemoteKeys((cafeLastId + diff), prevKey, nextKey)
                }
                database.remoteKeyDao.insertAll(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ContentEntity>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { content ->
            database.remoteKeyDao.getRemoteKeysId(content.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ContentEntity>): RemoteKeys? {
        return state.pages.lastOrNull() {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { content ->
            database.remoteKeyDao.getRemoteKeysId(content.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ContentEntity>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { remoteId ->
                database.remoteKeyDao.getRemoteKeysId(remoteId)
            }
        }
    }

}