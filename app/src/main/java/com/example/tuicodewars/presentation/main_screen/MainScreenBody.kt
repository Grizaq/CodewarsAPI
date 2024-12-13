package com.example.tuicodewars.presentation.main_screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.tuicodewars.R
import com.example.tuicodewars.data.model.user.DataJhoffner
import com.example.tuicodewars.presentation.commons.SpacerHeight
import com.example.tuicodewars.presentation.destinations.ListScreenDestination
import com.example.tuicodewars.presentation.ui.theme.Dimensions
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun MainScreenBody(itemUserData: DataJhoffner?, navigator: DestinationsNavigator) {
    val centerAlignment = Alignment.Center
    Column {
        SpacerHeight(height = Dimensions.spacerMedium)
        Box(modifier = Modifier.fillMaxWidth(), centerAlignment) {
            Column {
                Image(
                    painter = painterResource(
                        R.drawable._8747470733a2f2f646f63732e636f6465776172732e636f6d2f6c6f676f2e737667
                    ),
                    contentDescription = R.string.logo.toString(),
                    Modifier.fillMaxWidth(Dimensions.fillMainImage),
                    centerAlignment
                )
            }
        }
        SpacerHeight(height = Dimensions.spacerMedium)
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.name_creator, itemUserData!!.name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        SpacerHeight(height = Dimensions.spacerMedium)
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = centerAlignment
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(Dimensions.fillDefault),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(Dimensions.fillHalf),
                    contentAlignment = centerAlignment
                ) {
                    Text(
                        text = stringResource(
                            R.string.contributed_challenges,
                            itemUserData.codeChallenges.totalAuthored
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 2,
                        textAlign = TextAlign.Center
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = centerAlignment
                ) {
                    Text(
                        text = stringResource(
                            R.string.completed_challenges,
                            itemUserData.codeChallenges.totalCompleted
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 2,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        SpacerHeight(Dimensions.spacerLarge)
        Text(
            text = stringResource(R.string.list_of_languages_practiced),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.paddingMedium),
            contentAlignment = centerAlignment
        ) {
            val languageList = itemUserData.ranks.languages.keys.toList()
            Log.i("debugListItems", languageList.toString())
            LazyRow(Modifier.fillMaxWidth()) {
                items(items = languageList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimensions.paddingXSmall),
                        elevation = CardDefaults.cardElevation(Dimensions.elevationMedium)
                    ) {
                        Text(modifier = Modifier.padding(Dimensions.paddingXSmall), text = item)
                    }
                }
            }
        }
        SpacerHeight(Dimensions.spacerLarge)
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = centerAlignment) {
            Button(
                onClick = { navigator.navigate(ListScreenDestination) }, colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = ListItemDefaults.contentColor.copy(alpha = Dimensions.contentColor),
                    disabledContentColor = ListItemDefaults.contentColor.copy(alpha = Dimensions.contentColor)
                )
            ) {
                Text(
                    text = stringResource(R.string.btn_continue),
                    fontWeight = FontWeight.Bold,
                    fontSize = Dimensions.buttonTextSize
                )
            }
        }
        SpacerHeight(height = Dimensions.spacerLarge)
    }
}