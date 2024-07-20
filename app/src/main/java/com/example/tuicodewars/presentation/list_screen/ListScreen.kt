package com.example.tuicodewars.presentation.list_screen

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuicodewars.R
import com.example.tuicodewars.data.model.authored.Data
import com.example.tuicodewars.data.model.authored.Data.Companion.toCommaSeparatedString
import com.example.tuicodewars.domain.utils.Resource
import com.example.tuicodewars.presentation.commons.AppsTopAppBar
import com.example.tuicodewars.presentation.commons.PullToRefresh
import com.example.tuicodewars.presentation.commons.ShowErrorMessage
import com.example.tuicodewars.presentation.commons.ShowLoadingIndicator
import com.example.tuicodewars.presentation.destinations.ListDetailsDestination
import com.example.tuicodewars.presentation.view_models.ViewModelAuthored
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun ListScreen(
    viewModel: ViewModelAuthored = viewModel(LocalContext.current as ComponentActivity),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.authoredList.collectAsState()
    val challengesList = uiState.data?.data ?: emptyList()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.getAuthoredList() })
    val scope = rememberCoroutineScope()
    val listState = viewModel.scrollState

    Scaffold(topBar = {
        AppsTopAppBar(
            pageName = stringResource(R.string.scaffold_text_authored_list), navigator = navigator
        )
    }, content = { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .pullRefresh(pullRefreshState)
        ) {
            when (uiState) {
                is Resource.Success -> {
                    ListBody(challengesList, navigator, viewModel)
                    PullToRefresh(
                        isRefreshing = isRefreshing,
                        pullRefreshState = pullRefreshState,
                        Modifier.align(Alignment.TopCenter)
                    )
                    ScrollToTopButton(lazyListState = listState, scope = scope)
                }

                is Resource.Loading -> {
                    ShowLoadingIndicator()
                }

                is Resource.Error -> {
                    uiState.message?.let {
                        ShowErrorMessage(message = it, reload = { viewModel.getAuthoredList() })
                    }
                    val messageReload = stringResource(R.string.standard_reload_error_message)
                    LaunchedEffect(key1 = "") {
                        delay(5 * 1000)
                        viewModel.getAuthoredList()
                        Log.i("MainScreen", messageReload)
                    }
                }
            }
        }
    })
}

@Composable
private fun ListBody(
    challengesList: List<Data>, navigator: DestinationsNavigator, viewModel: ViewModelAuthored
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = viewModel.scrollState
    ) {
        items(challengesList) { item ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                onClick = {
                    navigator.navigate(ListDetailsDestination(challengeId = item.id))
                }) {
                Box(
                    modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = item.name,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(
                                R.string.list_rank, item.rank
                            ),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(
                                R.string.text_languages, item.languages.toCommaSeparatedString()
                            ), textAlign = TextAlign.Center, fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ScrollToTopButton(lazyListState: LazyListState, scope: CoroutineScope) {

    val showButton by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 2 && !lazyListState.isScrollInProgress
        }
    }
    AnimatedVisibility(
        visible = showButton,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp), Alignment.BottomCenter
        ) {
            Button(
                onClick = { scope.launch { lazyListState.animateScrollToItem(0) } },
                modifier = Modifier
                    .shadow(10.dp, shape = CircleShape)
                    .size(65.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                Icon(
                    Icons.Filled.KeyboardArrowUp,
                    contentDescription = "arrow up",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}
