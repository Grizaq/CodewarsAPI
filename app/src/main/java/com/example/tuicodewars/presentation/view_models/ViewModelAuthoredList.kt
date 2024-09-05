package com.example.tuicodewars.presentation.view_models

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuicodewars.data.model.authored.Authored
import com.example.tuicodewars.domain.repository.Repository
import com.example.tuicodewars.domain.usecases.CheckNetworkUseCase
import com.example.tuicodewars.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelAuthoredList @Inject constructor(
    private val repo: Repository, private val checkNetworkUseCase: CheckNetworkUseCase
) : ViewModel() {
    private val _authoredList: MutableStateFlow<Resource<Authored>> =
        MutableStateFlow(Resource.Loading())
    val authoredList: StateFlow<Resource<Authored>> = _authoredList

    val _bannerStateShow = MutableStateFlow(false)
    val bannerStateShow: StateFlow<Boolean> = _bannerStateShow

    private val _isRefreshingList = MutableStateFlow(false)
    val isRefreshingList: StateFlow<Boolean> = _isRefreshingList.asStateFlow()

    val scrollState = LazyListState()

    init {
        getAuthoredList()
    }

    // Re-fetch data for reload
    private fun updateAuthoredListState(resource: Resource<Authored>) {
        _authoredList.value = resource
        _isRefreshingList.value = false
        _bannerStateShow.value = when (resource) {
            is Resource.LocalData -> true
            else -> false
        }
    }

    fun getAuthoredList() = viewModelScope.launch {
        _isRefreshingList.value = true

        repo.getAuthoredList().collectLatest {
            updateAuthoredListState(it)
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            if (checkNetworkUseCase.isInternetAvailable()) {
                getAuthoredList() // Refresh data if internet is available
            } else {
                _bannerStateShow.value = true // Show banner if internet was turned off
                _isRefreshingList.value = false // End refreshing if no internet
            }
        }
    }

    fun hideBanner() {
        _bannerStateShow.value = false
    }
}