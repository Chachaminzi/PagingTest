package com.example.pagingtest.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.pagingtest.db.KeywordDao
import com.example.pagingtest.db.KeywordEntity
import com.example.pagingtest.db.SearchDatabase
import com.example.pagingtest.db.getDatabase
import com.example.pagingtest.models.Content
import com.example.pagingtest.repository.KakaoRepository
import com.example.pagingtest.repository.KeywordRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Database
     **/
    private val database = getDatabase(application)
    private val keywordRepository = KeywordRepository(database)

    val keywordList = keywordRepository.keywords

    // 리스트에 보일 content
    private val _content = MutableLiveData<Content>()
    val content: LiveData<Content> get() = _content

    private val _actionbarTitle = MutableLiveData<String>()
    val actionbarTitle: LiveData<String> get() = _actionbarTitle

    // action bar submit query
    val submitQuery = MutableLiveData<String>()

    fun updateContent(content: Content) {
        _content.postValue(content)
    }

    fun updateActionBarTitle(title: String) = _actionbarTitle.postValue(title)
    fun updateEmptyActionBarTitle() = _actionbarTitle.postValue("")

    /**
     * Submit Button click 감지
     **/
    private val _isSubmit = MutableLiveData<Boolean>()
    val isSubmit: LiveData<Boolean> get() = _isSubmit

    fun submitBtnClicked() {
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