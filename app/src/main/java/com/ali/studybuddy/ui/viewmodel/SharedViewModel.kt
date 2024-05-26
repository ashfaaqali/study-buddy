package com.ali.studybuddy.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _currentTabTitle = MutableLiveData<String>()
    val currentTabTitle: LiveData<String> get() = _currentTabTitle

    fun setCurrentTabTitle(title: String) {
        _currentTabTitle.value = title
    }
}
