package com.example.pagingtest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.flatMap
import androidx.paging.map
import com.example.pagingtest.api.BlogDocuments
import com.example.pagingtest.api.BlogSearchResponse
import com.example.pagingtest.api.CafeDocuments
import com.example.pagingtest.models.Content
import com.example.pagingtest.repository.KakaoRepository
import kotlinx.coroutines.flow.*

class ContentListViewModel(private val repository: KakaoRepository) : ViewModel() {

    private var currentCafeQuery: String? = null
    private var currentCafeResult: Flow<PagingData<Content>>? = null

    fun searchCafe(queryString: String): Flow<PagingData<Content>> {
        val lastResult = currentCafeResult
        if (queryString == currentCafeQuery && lastResult != null) {
            return lastResult
        }

        currentCafeQuery = queryString
        val newResult: Flow<PagingData<Content>> =
            repository.getCafeResultStream(queryString).cachedIn(viewModelScope)
        currentCafeResult = newResult
        return newResult
    }

    private var currentBlogQuery: String? = null
    private var currentBlogResult: Flow<PagingData<Content>>? = null

    fun searchBlog(queryString: String): Flow<PagingData<Content>> {
        val lastResult = currentBlogResult
        if (queryString == currentBlogQuery && lastResult != null) {
            return lastResult
        }

        currentBlogQuery = queryString
        val newResult: Flow<PagingData<Content>> =
            repository.getBlogResultStream(queryString).cachedIn(viewModelScope)
        currentBlogResult = newResult
        return newResult
    }


    // merge
    private var currentBothQuery: String? = null
    private var currentBothResult: Flow<PagingData<Content>>? = null

    fun searchContent(queryString: String): Flow<PagingData<Content>> {
        val lastResult = currentBothResult

        if (queryString == currentBothQuery && lastResult != null) {
            return lastResult
        }

        currentBothQuery = queryString
        val newBlogResult: Flow<PagingData<Content>> =
            repository.getBlogResultStream(queryString).cachedIn(viewModelScope)

        val newCafeResult: Flow<PagingData<Content>> =
            repository.getCafeResultStream(queryString).cachedIn(viewModelScope)

        val newResult = flowOf(newBlogResult, newCafeResult).flattenMerge()

        currentBothResult = newResult
        return newResult
    }

    /**
     * Spinner
     **/
    private val _spinnerSelected = MutableLiveData<Int>()
    val spinnerSelected: LiveData<Int> get() = _spinnerSelected

    fun updateSpinnerSelected(selected: Int) {
        _spinnerSelected.postValue(selected)
    }
}