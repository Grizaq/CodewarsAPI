package com.example.tuicodewars.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuicodewars.data.model.challenge.Challenge
import com.example.tuicodewars.domain.repository.Repository
import com.example.tuicodewars.domain.usecases.CheckNetworkUseCase
import com.example.tuicodewars.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelChallengeData @Inject constructor(
    private val repo: Repository, private val checkNetworkUseCase: CheckNetworkUseCase
) : ViewModel() {

    private val _challengeData: MutableStateFlow<Resource<Challenge>> =
        MutableStateFlow(Resource.Loading())
    val challengeData: StateFlow<Resource<Challenge>> = _challengeData

    val _bannerStateShow = MutableStateFlow(false)
    val bannerStateShow: StateFlow<Boolean> = _bannerStateShow

    private val _isRefreshingChallenge = MutableStateFlow(false)
    val isRefreshingChallenge: StateFlow<Boolean> = _isRefreshingChallenge

    // Initiate data fetch with the given challenge ID
    fun setChallengeId(id: String) {
        getChallengeData(id)
    }

    // Re-fetch data for reload
    private fun updateChallengeDataState(resource: Resource<Challenge>) {
        _challengeData.value = resource
        _isRefreshingChallenge.value = false
        _bannerStateShow.value = when (resource) {
            is Resource.LocalData -> true
            else -> false
        }
    }

    // Fetch challenge data from repository
    private fun getChallengeData(id: String) = viewModelScope.launch {
        _isRefreshingChallenge.value = true

        repo.getChallengeData(id).collectLatest {
            updateChallengeDataState(it)
        }
    }

    fun refreshData(id: String) = viewModelScope.launch {
        viewModelScope.launch {
            if (checkNetworkUseCase.isInternetAvailable()) {
                getChallengeData(id) // Refresh data if internet is available
            } else {
                _bannerStateShow.value = true // Show banner if no internet
                _isRefreshingChallenge.value = false // End refreshing if no internet
            }
        }
    }

    fun hideBanner() {
        _bannerStateShow.value = false
    }

    fun calculateCompletionRate(challengeData: Challenge?): String? {
        val totalAttempts = challengeData?.totalAttempts ?: 0
        val totalCompleted = challengeData?.totalCompleted ?: 0
        return if (totalAttempts != 0) {
            "%.2f".format(totalCompleted.toDouble() / totalAttempts * 100)
        } else null
    }
}
