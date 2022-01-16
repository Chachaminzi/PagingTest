package com.example.pagingtest.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pagingtest.api.KakaoService
import com.example.pagingtest.db.ContentEntity
import com.example.pagingtest.db.SearchDatabase
import com.example.pagingtest.repository.KakaoRepository
import okhttp3.internal.notify
import retrofit2.HttpException
import java.io.IOException

private const val KAKAO_STARTING_PAGE_INDEX = 1

@ExperimentalPagingApi
class KakaoRemoteMediator(
    private val apiQuery: String,
    private val database: SearchDatabase,
    private val service: KakaoService
) : RemoteMediator<Int, ContentEntity>() {
    private val contentDao = database.contentDao

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ContentEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    var beforeItemSize: Int? = null
                    database.withTransaction {
                        beforeItemSize = contentDao.getContentsSize()
                    }

                    beforeItemSize?: return MediatorResult.Success(endOfPaginationReached = true)
//                    val lastItem = state.lastItemOrNull()
//                        ?: return MediatorResult.Success(
//                            endOfPaginationReached = true
//                        )
//                    lastItem.id
                }
            }

            val position = loadKey ?: KAKAO_STARTING_PAGE_INDEX

            val response = service.searchCafe(
                query = apiQuery,
                page = position,
                size = KakaoRepository.NETWORK_PAGE_SIZE
            )
            val documents = response.documents.map {
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

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    contentDao.deleteAll()
                }
                contentDao.insertAll(documents)
            }

            MediatorResult.Success(
                endOfPaginationReached = (documents.isEmpty())
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}