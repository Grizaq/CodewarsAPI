package com.example.tuicodewars.presentation.list_details_screen

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuicodewars.R
import com.example.tuicodewars.data.model.authored.Data.Companion.toCommaSeparatedString
import com.example.tuicodewars.data.model.challenge.Challenge
import com.example.tuicodewars.data.model.challenge.Challenge.Companion.shortenLongWords
import com.example.tuicodewars.domain.utils.Resource
import com.example.tuicodewars.presentation.commons.AppsTopAppBar
import com.example.tuicodewars.presentation.commons.ShowErrorMessage
import com.example.tuicodewars.presentation.commons.ShowLoadingIndicator
import com.example.tuicodewars.presentation.commons.SpacerHeight
import com.example.tuicodewars.presentation.ui.theme.Dimensions
import com.example.tuicodewars.presentation.view_models.ViewModelChallengeData
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import java.util.Locale

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
    Scaffold(
        topBar = {
            AppsTopAppBar(
                pageName = stringResource(R.string.scaffold_text_challenge_details),
                navigator = navigator
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier.padding(padding)
            ) {
                when (uiState) {
                    is Resource.Success -> {
                        DetailsBody(challengeData, uriHandler)
                    }

                    is Resource.Loading -> {
                        ShowLoadingIndicator()
                    }

                    is Resource.Error -> {
                        uiState.message?.let {
                            ShowErrorMessage(
                                message = it,
                                reload = { viewModel.getChallengeData(challengeId) }
                            )
                        }
                        val messageReload = stringResource(R.string.standard_reload_error_message)
                        LaunchedEffect(key1 = "") {
                            delay(5 * 1000)
                            viewModel.setChallengeId(challengeId)
                            viewModel.getChallengeData(challengeId)
                            Log.i("MainScreen", messageReload)
                        }
                    }
                }
            }
        }
    )
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
            Text(
                text = "${challengeData?.name}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            if (challengeData?.totalAttempts != 0) {
                val completionRate =
                    challengeData?.totalCompleted!!.toDouble() / challengeData.totalAttempts.toDouble() * 100
                val formattedRate = String.format(Locale.US, "%.2f", completionRate).toDouble()

                SpacerHeight(height = Dimensions.spacerMedium)
                Text(text = "Completion rate of: $formattedRate%")
            }
            SpacerHeight(height = Dimensions.spacerMedium)
            Text(text = "Languages: ${challengeData.languages.toCommaSeparatedString()}")
            SpacerHeight(height = Dimensions.spacerMedium)
            Text(text = "Description: ${challengeData.description.shortenLongWords()}")
            SpacerHeight(height = Dimensions.spacerMedium)
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        uriHandler.openUri(challengeData.url)
                    },
                    colors = ButtonColors(
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
