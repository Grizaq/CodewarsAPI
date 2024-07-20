package com.example.tuicodewars.presentation.commons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.tuicodewars.R
import com.example.tuicodewars.presentation.ui.theme.Dimensions

/*
Error message for failed API response requests, with built in reload
reload: () -> Unit - reload function to call the api response
 */
@Composable
fun ShowErrorMessage(
    message: String,
    reload: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(
                text = stringResource(R.string.it_will_reload_soon, message),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { reload() },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(Dimensions.centerButtonWidth),
                ) {
                    Text(text = stringResource(R.string.reload_text), color = Color.White)
                }
            }
        }
    }
}