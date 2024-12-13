package com.example.tuicodewars.presentation.main_screen

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.testTag
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
import com.example.tuicodewars.presentation.view_models.ViewModelJhoffner
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun MainScreen(
    viewModel: ViewModelJhoffner = viewModel(LocalContext.current as ComponentActivity),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.itemList.collectAsState()
    val bannerStateShow by viewModel.bannerStateShow.collectAsState()
    val scrollState = rememberScrollState()
    val isRefreshing by viewModel.isRefreshingMain.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.refreshData() })


    Scaffold(topBar = {
        AppsTopAppBar(
            pageName = stringResource(R.string.scaffold_text_greeting_screen), navigator = navigator
        )
    }, content = { padding ->
        Box(
            modifier = Modifier
                .pullRefresh(pullRefreshState)
                .padding(padding)
                .testTag("pullRefresh")
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Banner showing data loaded from local storage
                LocalDataBanner(bannerStateShow, onDismiss = { viewModel.hideBanner() })

                Box(
                    modifier = Modifier.verticalScroll(scrollState)
                ) {
                    when (uiState) {
                        is Resource.Success, is Resource.LocalData -> {
                            MainScreenBody(uiState.data, navigator)
                        }

                        is Resource.Loading -> {
                            ShowLoadingIndicator()
                        }

                        is Resource.Error -> {
                            uiState.message?.let {
                                ShowErrorMessage(message = it, reload = { viewModel.getItemList() })
                            }
                            val messageReload =
                                stringResource(R.string.standard_reload_error_message)
                            LaunchedEffect(key1 = "") {
                                delay(5 * 1000)
                                viewModel.getItemList()
                                Log.i("MainScreen", messageReload)
                            }
                        }
                    }
                    PullToRefresh(
                        isRefreshing = isRefreshing,
                        pullRefreshState = pullRefreshState,
                        Modifier.align(Alignment.TopCenter)
                    )
                }
            }
        }
    })
}

