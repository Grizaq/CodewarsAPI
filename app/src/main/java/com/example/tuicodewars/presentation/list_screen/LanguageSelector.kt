package com.example.tuicodewars.presentation.list_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tuicodewars.presentation.ui.theme.Dimensions

@Composable
fun LanguageSelector(
    languages: List<String>,
    selectedLanguage: String?,
    onLanguageSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.padding(Dimensions.paddingMedium),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall)
    ) {
        items(languages) { language ->
            val isSelected =
                selectedLanguage == language || (selectedLanguage == null && language == "All")
            Button(
                onClick = { onLanguageSelected(if (language == "All") null else language) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.surface
                    else MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                contentPadding = PaddingValues(Dimensions.paddingSmall)
            ) {
                Text(language, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}