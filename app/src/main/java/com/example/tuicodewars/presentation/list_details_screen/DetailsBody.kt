package com.example.tuicodewars.presentation.list_details_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.tuicodewars.R
import com.example.tuicodewars.data.model.authored.Data.Companion.toCommaSeparatedString
import com.example.tuicodewars.data.model.challenge.Challenge
import com.example.tuicodewars.data.model.challenge.Challenge.Companion.shortenLongWords
import com.example.tuicodewars.presentation.commons.SpacerHeight
import com.example.tuicodewars.presentation.ui.theme.Dimensions
import com.example.tuicodewars.presentation.view_models.ViewModelChallengeData

@Composable
fun DetailsBody(
    challengeData: Challenge?,
    uriHandler: UriHandler,
    viewModel: ViewModelChallengeData
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth(Dimensions.fillDefault)
                .verticalScroll(rememberScrollState())
        ) {
            SpacerHeight(height = Dimensions.spacerMedium)

            // Handle nullability safely
            val name = challengeData?.name ?: "Unknown Challenge"
            val languages =
                challengeData?.languages?.toCommaSeparatedString() ?: "Unknown Languages"
            val description = challengeData?.description?.shortenLongWords() ?: "No Description"
            val url = challengeData?.url ?: ""

            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            val completionRate = viewModel.calculateCompletionRate(challengeData)
            if (completionRate != null) {
                SpacerHeight(height = Dimensions.spacerMedium)
                Text(text = "Completion rate of: $completionRate%")
            }

            SpacerHeight(height = Dimensions.spacerMedium)
            Text(text = "Languages: $languages")
            SpacerHeight(height = Dimensions.spacerMedium)
            Text(text = "Description: $description")
            SpacerHeight(height = Dimensions.spacerMedium)

            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (url.isNotBlank()) {
                            uriHandler.openUri(url)
                        }
                    }, colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = ListItemDefaults.contentColor.copy(alpha = Dimensions.contentColor),
                        disabledContentColor = ListItemDefaults.contentColor.copy(alpha = Dimensions.contentColor)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.btn_to_browser),
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimensions.buttonTextSize
                    )
                }
            }
            SpacerHeight(height = Dimensions.spacerMedium)
        }
    }
}