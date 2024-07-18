package com.example.tuicodewars.presentation.main_screen

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuicodewars.R
import com.example.tuicodewars.domain.utils.Resource
import com.example.tuicodewars.presentation.commons.ShowErrorMessage
import com.example.tuicodewars.presentation.commons.ShowLoadingIndicator
import com.example.tuicodewars.presentation.destinations.ListScreenDestination
import com.example.tuicodewars.presentation.view_models.ViewModelJhoffner
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

@RootNavGraph(start = true)
@Destination
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: ViewModelJhoffner = viewModel(LocalContext.current as ComponentActivity),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.itemList.collectAsState()
    val itemList = uiState.data
    Log.i("DebugnetworkCodeWars main screen", itemList.toString())
    when (uiState) {
        is Resource.Success -> {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxWidth(), Alignment.Center) {
                    Column {
                        Image(
                            painter = painterResource(R.drawable._8747470733a2f2f646f63732e636f6465776172732e636f6d2f6c6f676f2e737667),
                            contentDescription = R.string.logo.toString(),
                            Modifier.fillMaxWidth(0.75f),
                            Alignment.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Name: ${itemList?.name}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h4,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Contributed ${itemList!!.codeChallenges.totalAuthored} challenges",
                                style = MaterialTheme.typography.body1,
                                maxLines = 2,
                                textAlign = TextAlign.Center
                            )
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Completed ${itemList!!.codeChallenges.totalCompleted} challenges",
                                style = MaterialTheme.typography.body1,
                                maxLines = 2,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "List of languages practiced:",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = { navigator.navigate(ListScreenDestination) }) {
                        Text(text = "Continue")
                    }
                }
            }
        }

        is Resource.Loading -> {
            ShowLoadingIndicator()
        }

        is Resource.Error -> {
            uiState.message?.let {
                ShowErrorMessage(
                    message = it, reload = { viewModel.getItemList() }
                )
            }
            val messageReload = stringResource(R.string.standard_reload_error_message)
            LaunchedEffect(key1 = "") {
                delay(5 * 1000)
                viewModel.getItemList()
                Log.i("MainScreen", messageReload)
            }
        }
    }
}