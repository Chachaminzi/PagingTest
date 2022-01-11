package com.example.pagingtest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pagingtest.models.Content

class MainViewModel : ViewModel() {

    private val _content = MutableLiveData<Content>()
    val content: LiveData<Content> get() = _content

    private val _actionbarTitle = MutableLiveData<String>()
    val actionbarTitle: LiveData<String> get() = _actionbarTitle

    fun updateContent(content: Content) {
        _content.postValue(content)
    }

    fun updateActionBarTitle(title: String) = _actionbarTitle.postValue(title)
    fun updateEmptyActionBarTitle() = _actionbarTitle.postValue("")
}