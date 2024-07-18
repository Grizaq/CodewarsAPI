package com.example.tuicodewars.presentation.list_details_screen

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuicodewars.R
import com.example.tuicodewars.domain.utils.Resource
import com.example.tuicodewars.presentation.commons.ShowErrorMessage
import com.example.tuicodewars.presentation.commons.ShowLoadingIndicator
import com.example.tuicodewars.presentation.view_models.ViewModelChallengeData
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

@Destination
@Composable
fun ListDetails(
    modifier: Modifier = Modifier,
    viewModel: ViewModelChallengeData = viewModel(LocalContext.current as ComponentActivity),
    navigator: DestinationsNavigator,
    challengeId: String
) {
    LaunchedEffect(challengeId) {
    viewModel.setChallengeId(challengeId)
    }
    val uiState by viewModel.challengeData.collectAsState()
    val challengeData = uiState.data
    Log.i("DebugnetworkCodeWars details list screen", challengeData.toString())
    when (uiState) {
        is Resource.Success -> {
            Text(text = "${challengeData?.name}")
        }

        is Resource.Loading -> {
            ShowLoadingIndicator()
        }

        is Resource.Error -> {
            uiState.message?.let {
                ShowErrorMessage(message = it, reload = { viewModel.getChallengeData("asd") })
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