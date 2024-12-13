package com.example.tuicodewars.presentation.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import com.example.tuicodewars.presentation.ui.theme.Dimensions
import com.example.tuicodewars.presentation.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScrollToTopButton(lazyListState: LazyListState, scope: CoroutineScope) {
    val showButton by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 2 && !lazyListState.isScrollInProgress
        }
    }
    AnimatedVisibility(
        visible = showButton, enter = fadeIn(), exit = fadeOut()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = Dimensions.paddingLarge), Alignment.BottomCenter
        ) {
            Button(
                onClick = { scrollToTop(scope, lazyListState) },
                modifier = Modifier
                    .shadow(Dimensions.shadowMedium, shape = CircleShape)
                    .size(Dimensions.toTopButtonSize),
                contentPadding = PaddingValues(Dimensions.paddingXSmall),
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = contentColor.copy(alpha = Dimensions.contentColor),
                    disabledContentColor = contentColor.copy(alpha = Dimensions.contentColor)
                )
            ) {
                Icon(
                    Icons.Filled.KeyboardArrowUp,
                    contentDescription = "arrow up",
                    tint = White,
                    modifier = Modifier.size(Dimensions.toTopArrowSize)
                )
            }
        }
    }
}

fun scrollToTop(scope: CoroutineScope, lazyListState: LazyListState) {
    scope.launch {
        lazyListState.animateScrollToItem(0)
    }
}