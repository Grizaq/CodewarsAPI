package com.example.tuicodewars.presentation.commons

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/*
Simple pull to refresh builder
 */
@Composable
@OptIn(ExperimentalMaterialApi::class)
fun PullToRefresh(isRefreshing: Boolean, pullRefreshState: PullRefreshState, modifier: Modifier) {
    PullRefreshIndicator(refreshing = isRefreshing, state = pullRefreshState, modifier = modifier)
}
