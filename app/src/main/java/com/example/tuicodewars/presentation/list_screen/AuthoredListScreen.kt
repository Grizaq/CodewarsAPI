package com.example.tuicodewars.presentation.list_screen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuicodewars.R
import com.example.tuicodewars.domain.utils.Resource
import com.example.tuicodewars.presentation.commons.AppsTopAppBar
import com.example.tuicodewars.presentation.commons.PullToRefresh
import com.example.tuicodewars.presentation.commons.ShowErrorMessage
import com.example.tuicodewars.presentation.commons.ShowLoadingIndicator
import com.example.tuicodewars.presentation.utils.ScrollToTopButton
import com.example.tuicodewars.presentation.commons.LocalDataBanner
import com.example.tuicodewars.presentation.view_models.ViewModelAuthoredList
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun ListScreen(
    viewModel: ViewModelAuthoredList = viewModel(LocalContext.current as ComponentActivity),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.authoredList.collectAsState()
    val challengesList = uiState.data?.data ?: emptyList()
    val isRefreshing by viewModel.isRefreshingList.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.refreshData() })
    val scope = rememberCoroutineScope()
    val listState = viewModel.scrollState
    val bannerStateShow by viewModel.bannerStateShow.collectAsState()
    val allLanguages by viewModel.availableLanguages.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()

    Scaffold(topBar = {
        AppsTopAppBar(
            pageName = stringResource(R.string.scaffold_text_authored_list), navigator = navigator
        )
    }, content = { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Banner showing data loaded from local storage
            LocalDataBanner(bannerStateShow, onDismiss = { viewModel.hideBanner() })

            LanguageSelector(languages = allLanguages,
                selectedLanguage = selectedLanguage,
                onLanguageSelected = { selectedLang ->
                    viewModel.setSelectedLanguage(selectedLang)
                    viewModel.scrollToTop(scope)
                })

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                when (uiState) {
                    is Resource.Success, is Resource.LocalData -> {
                        ListBody(challengesList, navigator, viewModel)
                        ScrollToTopButton(lazyListState = listState, scope = scope)
                    }

                    is Resource.Loading -> {
                        ShowLoadingIndicator()
                    }

                    is Resource.Error -> {
                        uiState.message?.let {
                            ShowErrorMessage(message = it, reload = { viewModel.refreshData() })
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
    })
}
