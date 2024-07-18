package com.example.tuicodewars.presentation.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuicodewars.data.model.challenge.Challenge
import com.example.tuicodewars.data.repository.Repository
import com.example.tuicodewars.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelChallengeData @Inject constructor(
    private val repo: Repository
) : ViewModel() {
    private val _challengeData: MutableStateFlow<Resource<Challenge>> =
        MutableStateFlow(Resource.Loading())
    val challengeData: StateFlow<Resource<Challenge>> = _challengeData

    init {
        getChallengeData("asd")
    }

    fun getChallengeData(id: String) = viewModelScope.launch {
        repo.getChallengeData(id).collectLatest {
            Log.i("DebugnetworkCodeWars view model challenge", challengeData.value.data.toString())
            _challengeData.emit(it)
        }
    }

}