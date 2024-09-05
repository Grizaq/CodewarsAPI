package com.example.tuicodewars.presentation.list_details_screen

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuicodewars.R
import com.example.tuicodewars.data.model.authored.Data.Companion.toCommaSeparatedString
import com.example.tuicodewars.data.model.challenge.Challenge
import com.example.tuicodewars.data.model.challenge.Challenge.Companion.shortenLongWords
import com.example.tuicodewars.domain.utils.Resource
import com.example.tuicodewars.presentation.commons.AppsTopAppBar
import com.example.tuicodewars.presentation.commons.Banner
import com.example.tuicodewars.presentation.commons.PullToRefresh
import com.example.tuicodewars.presentation.commons.ShowErrorMessage
import com.example.tuicodewars.presentation.commons.ShowLoadingIndicator
import com.example.tuicodewars.presentation.commons.SpacerHeight
import com.example.tuicodewars.presentation.ui.theme.Dimensions
import com.example.tuicodewars.presentation.view_models.ViewModelChallengeData
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import java.util.Locale

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
            if (bannerStateShow) {
                Banner(message = stringResource(R.string.banner_no_internet),
                    onDismiss = { viewModel.hideBanner() })
            }
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
                        DetailsBody(challengeData, uriHandler)
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

@Composable
private fun DetailsBody(challengeData: Challenge?, uriHandler: UriHandler) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth(Dimensions.fillDefault)
                .verticalScroll(rememberScrollState())
        ) {
            SpacerHeight(height = Dimensions.spacerMedium)

            // Handle nullability safely
            val name = challengeData?.name ?: "Unknown Challenge"
            val totalAttempts = challengeData?.totalAttempts ?: 0
            val totalCompleted = challengeData?.totalCompleted ?: 0
            val languages =
                challengeData?.languages?.toCommaSeparatedString() ?: "Unknown Languages"
            val description = challengeData?.description?.shortenLongWords() ?: "No Description"
            val url = challengeData?.url ?: ""

            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            if (totalAttempts != 0) {
                val completionRate = totalCompleted.toDouble() / totalAttempts.toDouble() * 100
                val formattedRate = String.format(Locale.US, "%.2f", completionRate)

                SpacerHeight(height = Dimensions.spacerMedium)
                Text(text = "Completion rate of: $formattedRate%")
            }

            SpacerHeight(height = Dimensions.spacerMedium)
            Text(text = "Languages: $languages")
            SpacerHeight(height = Dimensions.spacerMedium)
            Text(text = "Description: $description")
            SpacerHeight(height = Dimensions.spacerMedium)

            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (url.isNotBlank()) {
                            uriHandler.openUri(url)
                        }
                    }, colors = ButtonColors(
                        Color.Blue,
                        contentColor = Color.White,
                        disabledContainerColor = contentColor.copy(alpha = Dimensions.contentColor),
                        disabledContentColor = contentColor.copy(alpha = Dimensions.contentColor)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.btn_to_browser),
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimensions.buttonTextSize
                    )
                }
            }
            SpacerHeight(height = Dimensions.spacerMedium)
        }
    }
}