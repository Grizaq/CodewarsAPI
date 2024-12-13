package com.example.tuicodewars.presentation.list_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.util.lerp
import com.example.tuicodewars.R
import com.example.tuicodewars.data.model.authored.Data
import com.example.tuicodewars.data.model.authored.Data.Companion.toCommaSeparatedString
import com.example.tuicodewars.domain.utils.generateLogoUrlsForLanguages
import com.example.tuicodewars.presentation.commons.SpacerHeight
import com.example.tuicodewars.presentation.destinations.ListDetailsDestination
import com.example.tuicodewars.presentation.ui.theme.Dimensions
import com.example.tuicodewars.presentation.view_models.ViewModelAuthoredList
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.glide.GlideImage
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ListBody(
    challengesList: List<Data>,
    navigator: DestinationsNavigator,
    viewModel: ViewModelAuthoredList
) {
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()

    val filteredList = if (selectedLanguage == null) {
        challengesList
    } else {
        challengesList.filter { it.languages.contains(selectedLanguage) }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(), state = viewModel.scrollState
    ) {
        items(filteredList) { item ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Dimensions.paddingXSmall, horizontal = Dimensions.paddingMedium
                ),
                elevation = CardDefaults.cardElevation(Dimensions.elevationMedium),
                onClick = {
                    navigator.navigate(ListDetailsDestination(challengeId = item.id))
                }) {
                Box(
                    modifier = Modifier.padding(Dimensions.paddingMedium),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Generate logo URLs for all languages
                        val logoUrls = item.languages.generateLogoUrlsForLanguages()

                        // Create a pager state
                        val pagerState = rememberPagerState(initialPage = 0)


                        // HorizontalPager to display multiple logos
                        HorizontalPager(
                            count = logoUrls.size, // Number of logos
                            state = pagerState,
                            modifier = Modifier
                                .height(Dimensions.pagerHeight)
                                .fillMaxWidth()
                        ) { page ->
                            // Display each logo in the pager
                            Card(shape = RoundedCornerShape(Dimensions.paddingSmall),
                                modifier = Modifier.graphicsLayer {
                                    val pageOffset =
                                        calculateCurrentOffsetForPage(page).absoluteValue
                                    lerp(
                                        start = 0.85f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    ).also { scale ->
                                        scaleX = scale
                                        scaleY = scale
                                    }
                                    alpha = lerp(
                                        start = 0.5f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    )
                                }) {
                                GlideImage(
                                    imageModel = logoUrls[page], // Display logo for each page
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(Dimensions.roundingPercent))
                                        .fillMaxHeight()
                                        .fillMaxWidth()
                                )
                            }
                        }

                        // Pager Indicator for logos
                        HorizontalPagerIndicator(
                            pagerState = pagerState,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(Dimensions.paddingMedium)
                        )

                        Text(
                            text = item.name,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        SpacerHeight(Dimensions.spacerSmall)
                        Text(
                            text = stringResource(
                                R.string.list_rank, item.rank
                            ),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        SpacerHeight(Dimensions.spacerSmall)
                        Text(
                            text = stringResource(
                                R.string.text_languages, item.languages.toCommaSeparatedString()
                            ), textAlign = TextAlign.Center, fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}