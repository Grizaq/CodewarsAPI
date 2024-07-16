package com.example.tuicodewars.presentation.main_screen

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
    val name = itemList?.name
    when (uiState) {
        is Resource.Success -> {

            Text(
                text = "Hello $name!",
                modifier = modifier
            )
        }

        is Resource.Loading -> {
            ShowLoadingIndicator()
        }

        is Resource.Error -> {
            uiState.message?.let {
                ShowErrorMessage(
                    message = it, viewModel = viewModel
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