package com.example.tuicodewars.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuicodewars.data.model.user.DataJhoffner
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
class ViewModelJhoffner @Inject constructor(
    private val repo: Repository, private val checkNetworkUseCase: CheckNetworkUseCase
) : ViewModel() {

    private val _itemList: MutableStateFlow<Resource<DataJhoffner>> = MutableStateFlow(Resource.Loading())
    val itemList: StateFlow<Resource<DataJhoffner>> = _itemList

    val _bannerStateShow = MutableStateFlow(false)
    val bannerStateShow: StateFlow<Boolean> = _bannerStateShow

    private val _isRefreshingMain = MutableStateFlow(false)
    val isRefreshingMain: StateFlow<Boolean> = _isRefreshingMain.asStateFlow()

    init {
        getItemList()
    }

    // Re-fetch data for reload
    private fun updateItemListState(resource: Resource<DataJhoffner>) {
        _itemList.value = resource
        _isRefreshingMain.value = false
        _bannerStateShow.value = when (resource) {
            is Resource.LocalData -> true
            is Resource.Error -> true
            else -> false
        }
    }

    fun getItemList() = viewModelScope.launch {
        _isRefreshingMain.value = true

        repo.getItemsList().collectLatest {
            updateItemListState(it)
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            if (checkNetworkUseCase.isInternetAvailable()) {
                getItemList() // Refresh data if internet is available
            } else {
                _bannerStateShow.value = true // Show banner if no internet
                _isRefreshingMain.value = false // End refreshing if no internet
            }
        }
    }

    fun hideBanner() {
        _bannerStateShow.value = false
    }
}
