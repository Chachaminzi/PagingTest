package com.example.pagingtest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pagingtest.api.CafeDocuments
import com.example.pagingtest.repository.KakaoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class ContentListViewModel(private val repository: KakaoRepository) : ViewModel() {

    private var currentQuery: String? = null

    private var currentCafeResult: Flow<PagingData<CafeDocuments>>? = null

    fun searchCafe(queryString: String): Flow<PagingData<CafeDocuments>> {
        val lastResult = currentCafeResult
        if (queryString == currentQuery && lastResult != null) {
            return lastResult
        }

        currentQuery = queryString
        val newResult: Flow<PagingData<CafeDocuments>> =
            repository.getCafeResultStream(queryString).cachedIn(viewModelScope)
        currentCafeResult = newResult
        return newResult
    }
}