package com.example.tuicodewars.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuicodewars.data.model.user.DataJhoffner
import com.example.tuicodewars.data.repository.Repository
import com.example.tuicodewars.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelJhoffner @Inject constructor(
    private val repo: Repository
) : ViewModel() {
    private val _itemList: MutableStateFlow<Resource<DataJhoffner>> =
        MutableStateFlow(Resource.Loading())
    val itemList: StateFlow<Resource<DataJhoffner>> = _itemList

    init {
        getItemList()
    }

    fun getItemList() = viewModelScope.launch {
        repo.getItemsList().collectLatest {
            _itemList.emit(it)
        }
    }
}