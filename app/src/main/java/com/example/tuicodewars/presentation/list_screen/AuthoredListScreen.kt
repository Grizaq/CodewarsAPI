package com.example.tuicodewars.presentation.list_screen

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuicodewars.R
import com.example.tuicodewars.data.model.authored.Data
import com.example.tuicodewars.data.model.authored.Data.Companion.toCommaSeparatedString
import com.example.tuicodewars.domain.utils.Resource
import com.example.tuicodewars.domain.utils.generateLogoUrlsForLanguages
import com.example.tuicodewars.presentation.commons.AppsTopAppBar
import com.example.tuicodewars.presentation.commons.Banner
import com.example.tuicodewars.presentation.commons.PullToRefresh
import com.example.tuicodewars.presentation.commons.ShowErrorMessage
import com.example.tuicodewars.presentation.commons.ShowLoadingIndicator
import com.example.tuicodewars.presentation.commons.SpacerHeight
import com.example.tuicodewars.presentation.destinations.ListDetailsDestination
import com.example.tuicodewars.presentation.ui.theme.Dimensions
import com.example.tuicodewars.presentation.view_models.ViewModelAuthoredList
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

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

    Scaffold(
        topBar = {
            AppsTopAppBar(
                pageName = stringResource(R.string.scaffold_text_authored_list),
                navigator = navigator
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                // Banner showing data loaded from local storage
                if (bannerStateShow) {
                    Banner(
                        message = stringResource(R.string.banner_no_internet),
                        onDismiss = { viewModel.hideBanner() }
                    )
                }
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
                                ShowErrorMessage(
                                    message = it,
                                    reload = { viewModel.refreshData() }
                                )
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
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ListBody(
    challengesList: List<Data>,
    navigator: DestinationsNavigator,
    viewModel: ViewModelAuthoredList
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = viewModel.scrollState
    ) {
        items(challengesList) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = Dimensions.paddingXSmall,
                        horizontal = Dimensions.paddingMedium
                    ),
                elevation = CardDefaults.cardElevation(Dimensions.elevationMedium),
                onClick = {
                    navigator.navigate(ListDetailsDestination(challengeId = item.id))
                }
            ) {
                Box(
                    modifier = Modifier.padding(Dimensions.paddingMedium),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Generate logo URLs for all languages
                        val logoUrls = item.languages.generateLogoUrlsForLanguages()

                        // Create a pager state
                        val pagerState = rememberPagerState(initialPage = 0)


                        // HorizontalPager to display multiple logos
                        HorizontalPager(
                            count = logoUrls.size, // Number of logos
                            state = pagerState,
                            modifier = Modifier
                                .height(Dimensions.pagerHeight) // Set your desired pager height
                                .fillMaxWidth()
                        ) { page ->
                            // Display each logo in the pager
                            Card(shape = RoundedCornerShape(Dimensions.paddingSmall),
                                modifier = Modifier.graphicsLayer {
                                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                                    lerp(
                                        start = 0.85f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    ).also { scale ->
                                        scaleX = scale
                                        scaleY = scale
                                    }
                                    alpha = lerp(
                                        start = 0.5f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    )
                                }) {
                                GlideImage(
                                    imageModel = logoUrls[page], // Display logo for each page
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(Dimensions.roundingPercent))
                                        .fillMaxHeight()
                                        .fillMaxWidth(Dimensions.fillDefault),
                                )
                            }
                        }

                        // Pager Indicator for logos
                        HorizontalPagerIndicator(
                            pagerState = pagerState,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(Dimensions.paddingMedium)
                        )

                        Text(
                            text = item.name,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        SpacerHeight(Dimensions.spacerSmall)
                        Text(
                            text = stringResource(
                                R.string.list_rank,
                                item.rank
                            ),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        SpacerHeight(Dimensions.spacerSmall)
                        Text(
                            text = stringResource(
                                R.string.text_languages,
                                item.languages.toCommaSeparatedString()
                            ),
                            textAlign = TextAlign.Center,
                            fontStyle = FontStyle.Italic
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
        exit = fadeOut()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = Dimensions.paddingLarge),
            Alignment.BottomCenter
        ) {
            Button(
                onClick = { scope.launch { lazyListState.animateScrollToItem(0) } },
                modifier = Modifier
                    .shadow(Dimensions.shadowMedium, shape = CircleShape)
                    .size(Dimensions.toTopButtonSize),
                contentPadding = PaddingValues(Dimensions.paddingXSmall),
                colors = ButtonColors(
                    Color.Blue,
                    contentColor = Color.White,
                    disabledContainerColor = contentColor.copy(alpha = Dimensions.contentColor),
                    disabledContentColor = contentColor.copy(alpha = Dimensions.contentColor)
                )
            ) {
                Icon(
                    Icons.Filled.KeyboardArrowUp,
                    contentDescription = "arrow up",
                    tint = Color.White,
                    modifier = Modifier.size(Dimensions.toTopArrowSize)
                )
            }
        }
    }
}
