package com.example.tuicodewars.presentation.commons

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig

@Composable
fun rememberDynamicTitle(remoteConfigKey: String, defaultTitleRes: Int): MutableState<String> {
    // Firebase Remote Config
    val context = LocalContext.current
    val remoteConfig = remember { Firebase.remoteConfig }
    val dynamicTitle = remember(remoteConfigKey) {
        mutableStateOf(context.getString(defaultTitleRes))
    }

    // Fetch remote config value
    LaunchedEffect(remoteConfigKey) {
        remoteConfig.setDefaultsAsync(mapOf(remoteConfigKey to dynamicTitle.value))
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val fetchedTitle = remoteConfig.getString(remoteConfigKey)
                Log.d("RemoteConfigTitle", fetchedTitle)
                if (fetchedTitle.isNotEmpty()) {
                    dynamicTitle.value = fetchedTitle
                }
            } else {
                Log.e("RemoteConfig", "Failed to fetch config: ${task.exception}")
            }
        }
    }

    return dynamicTitle
}
