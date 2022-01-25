package com.example.pagingtest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pagingtest.db.KeywordEntity
import com.example.pagingtest.models.Content
import com.example.pagingtest.models.ItemModel
import com.example.pagingtest.repository.KakaoRepository
import com.example.pagingtest.repository.KeywordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentListViewModel @Inject constructor(
    private val kakaoRepository: KakaoRepository,
    private val keywordRepository: KeywordRepository
) : ViewModel() {

    var selectedPostType = 0

    private val _filterType = MutableLiveData(0)
    val filterType: LiveData<Int> get() = _filterType

    fun updateFilter(type: Int) {
        _filterType.postValue(type)
    }

    private fun getFilterTypeString(): String {
        return if (filterType.value == 0) "accuracy" else "recency"
    }

    /**
     * Paging
     **/
    private var currentFilterType: Int = 0

    private var currentCafeQuery: String? = null
    private var currentCafeResult: Flow<PagingData<Content>>? = null

    private fun searchCafe(queryString: String): Flow<PagingData<Content>> {
        val lastResult = currentCafeResult
        if (queryString == currentCafeQuery && currentFilterType == filterType.value && lastResult != null) {
            return lastResult
        }

        currentFilterType = filterType.value!!
        currentCafeQuery = queryString
        val newResult: Flow<PagingData<Content>> =
            kakaoRepository.getCafeResultStream(queryString, getFilterTypeString())
                .cachedIn(viewModelScope)

        currentCafeResult = newResult
        return newResult
    }

    private var currentBlogQuery: String? = null
    private var currentBlogResult: Flow<PagingData<Content>>? = null

    private fun searchBlog(queryString: String): Flow<PagingData<Content>> {
        val lastResult = currentBlogResult
        if (queryString == currentBlogQuery && currentFilterType == filterType.value && lastResult != null) {
            return lastResult
        }

        currentBlogQuery = queryString
        val newResult: Flow<PagingData<Content>> =
            kakaoRepository.getBlogResultStream(queryString, getFilterTypeString())
                .cachedIn(viewModelScope)

        currentBlogResult = newResult
        return newResult
    }
    
    private var _itemPagingData = MutableLiveData<PagingData<Content>>()
    val itemPagingData: LiveData<PagingData<Content>> get() = _itemPagingData

    suspend fun loadList() {
        submitQuery.value?.let { query ->
            when (selectedPostType) {
                0 -> {
//                    contentViewModel.searchAll(query).collectLatest {
//                        recyclerAdapter.submitData(it)
//                    }
                }
                1 -> {
                    searchBlog(query).collectLatest {
                        _itemPagingData.postValue(it)
                    }
                }
                2 -> {
                    searchCafe(query).collectLatest {
                        _itemPagingData.postValue(it)
                    }
                }
            }
        }
    }

    /**
     * 검색어
     **/
    val keywordList = keywordRepository.keywords

    // tool bar Submit query
    val submitQuery = MutableLiveData<String>()
    private val _isSubmit = MutableLiveData<Boolean>()
    val isSubmit: LiveData<Boolean> get() = _isSubmit

    fun updateSubmitQuery(position: Int) {
        submitQuery.postValue(keywordList.value?.get(position))
        _isSubmit.postValue(true)
    }

    fun updateSpinnerSelected(position: Int) {
        selectedPostType = position

        if (!submitQuery.value.isNullOrBlank()) {
            viewModelScope.launch {
                keywordRepository.updateKeyword(
                    KeywordEntity(
                        recordName = submitQuery.value.toString(),
                        recordTime = System.currentTimeMillis()
                    )
                )
                keywordRepository.deleteKeywordOverLimit()
                _isSubmit.postValue(true)
            }
        }
    }
}