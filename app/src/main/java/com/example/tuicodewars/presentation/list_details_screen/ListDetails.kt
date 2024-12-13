package com.example.tuicodewars.presentation.list_details_screen

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuicodewars.R
import com.example.tuicodewars.domain.utils.Resource
import com.example.tuicodewars.presentation.commons.AppsTopAppBar
import com.example.tuicodewars.presentation.commons.PullToRefresh
import com.example.tuicodewars.presentation.commons.ShowErrorMessage
import com.example.tuicodewars.presentation.commons.ShowLoadingIndicator
import com.example.tuicodewars.presentation.commons.LocalDataBanner
import com.example.tuicodewars.presentation.view_models.ViewModelChallengeData
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun ListDetails(
    viewModel: ViewModelChallengeData = viewModel(LocalContext.current as ComponentActivity),
    navigator: DestinationsNavigator,
    challengeId: String
) {
    LaunchedEffect(challengeId) {
        viewModel.setChallengeId(challengeId)
    }

    val uiState by viewModel.challengeData.collectAsState()
    val challengeData = uiState.data
    val uriHandler = LocalUriHandler.current
    val bannerStateShow by viewModel.bannerStateShow.collectAsState()
    val isRefreshing by viewModel.isRefreshingChallenge.collectAsStateWithLifecycle()
    val pullRefreshState =
        rememberPullRefreshState(isRefreshing, { viewModel.refreshData(challengeId) })

    Scaffold(topBar = {
        AppsTopAppBar(
            pageName = stringResource(R.string.scaffold_text_challenge_details),
            navigator = navigator
        )
    }, content = { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Banner showing data loaded from local storage
            LocalDataBanner(bannerStateShow, onDismiss = { viewModel.hideBanner() })
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                PullToRefresh(
                    isRefreshing = isRefreshing,
                    pullRefreshState = pullRefreshState,
                    Modifier.align(Alignment.TopCenter)
                )
                when (uiState) {
                    is Resource.Success, is Resource.LocalData -> {
                        DetailsBody(challengeData, uriHandler, viewModel)
                    }

                    is Resource.Loading -> {
                        ShowLoadingIndicator()
                    }

                    is Resource.Error -> {
                        uiState.message?.let {
                            ShowErrorMessage(message = it,
                                reload = { viewModel.refreshData(challengeId) })
                        }
                        val messageReload = stringResource(R.string.standard_reload_error_message)
                        LaunchedEffect(key1 = "error_reload") {
                            delay(5 * 1000) // Retry delay
                            viewModel.refreshData(challengeId)
                            Log.i("ListDetailsScreen", messageReload)
                        }
                    }
                }
            }
        }
    })
}
