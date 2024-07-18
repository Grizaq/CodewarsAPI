package com.example.tuicodewars.presentation.list_screen

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuicodewars.R
import com.example.tuicodewars.domain.utils.Resource
import com.example.tuicodewars.presentation.commons.ShowErrorMessage
import com.example.tuicodewars.presentation.commons.ShowLoadingIndicator
import com.example.tuicodewars.presentation.ui.theme.Dimensions
import com.example.tuicodewars.presentation.view_models.ViewModelAuthored
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

@RootNavGraph(start = true)
@Destination
@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    viewModel: ViewModelAuthored = viewModel(LocalContext.current as ComponentActivity),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.authoredList.collectAsState()
    val challengesList = uiState.data?.data ?: emptyList()
    Log.i("DebugnetworkCodeWars list screen", challengesList.toString())
    when (uiState) {
        is Resource.Success -> {
//            Text(text = "Hello ${challengesList[0].description}")
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(challengesList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
//                        onClick = {
//                        }
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = item.id)
                                Text(text = item.name)
                                Text(text = item.rankName)
                            }
                        }
                    }
                }
            }
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