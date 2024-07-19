package com.example.tuicodewars.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuicodewars.data.model.authored.Authored
import com.example.tuicodewars.data.repository.Repository
import com.example.tuicodewars.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelAuthored @Inject constructor(
    private val repo: Repository
) : ViewModel() {
    private val _authoredList: MutableStateFlow<Resource<Authored>> =
        MutableStateFlow(Resource.Loading())
    val authoredList: StateFlow<Resource<Authored>> = _authoredList

    init {
        getAuthoredList()
    }

    fun getAuthoredList() = viewModelScope.launch {
        repo.getAuthoredList().collectLatest {
            _authoredList.emit(it)
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
}