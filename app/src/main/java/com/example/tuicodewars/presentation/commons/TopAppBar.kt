package com.example.tuicodewars.presentation.commons

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuicodewars.R
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/*
Top app bar to be added to all pages for consistency, and on screen navigation
Uses ComposeDestinations library's navigator
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsTopAppBar(
    pageName: String,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val eMailTopic = stringResource(
        R.string.message_about, pageName
    )
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = Color.Blue,
            Color.Transparent,
            Color.Transparent,
            Color.White,
            Color.Transparent
        ),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .offset(x = (-12).dp), contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_desc),
                        )
                    }
                    Text(
                        text = pageName,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .background(Color.Transparent),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                    IconButton(onClick = {
                        context.sendMail(
                            to = "example@gmail.com", // To be adjusted to the required receiver e.g. support
                            subject = eMailTopic // Autofill for e-mail subject
                        )
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = stringResource(R.string.back_button_desc),
                        )
                    }
                }
            }
        },
    )
}

fun Context.sendMail(to: String, subject: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "vnd.android.cursor.item/email"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Log.e("TopAppBar", "error sending message: $e")
    } catch (t: Throwable) {
        Log.e("TopAppBar", "sending message throws: $t")
    }
}
