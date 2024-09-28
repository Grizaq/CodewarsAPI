package com.example.tuicodewars.presentation.main_screen

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.navigation.compose.rememberNavController
import com.example.tuicodewars.domain.usecases.CheckNetworkUseCase
import com.example.tuicodewars.presentation.view_models.ViewModelJhoffner
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainScreenIntegrationTest {

    @get:Rule
    var composeTestRule = createComposeRule()

    private lateinit var fakeRepository: FakeRepository
    private lateinit var navigatorMock: NavigatorMock
    private lateinit var viewModel: ViewModelJhoffner
    private lateinit var fakeNetworkChecker: FakeNetworkChecker
    private lateinit var checkNetworkUseCase: CheckNetworkUseCase

    @Before
    fun setUp() {
        fakeRepository = FakeRepository()
        fakeNetworkChecker = FakeNetworkChecker()
        checkNetworkUseCase = CheckNetworkUseCase(fakeNetworkChecker)

        viewModel = ViewModelJhoffner(fakeRepository, checkNetworkUseCase)
    }

    @Test
    fun loadingState_displaysLoadingIndicator() {
        runBlocking {
            // Trigger the loading state
            fakeRepository.setTestScenario(FakeRepository.TestScenario.Loading)

            composeTestRule.setContent {
                val navController = rememberNavController()
                navigatorMock = NavigatorMock(navController)
                MainScreen(viewModel = viewModel, navigator = navigatorMock)
            }

            // Delay to resolve test flakiness caused by instant loading not allowing state change
            delay(150)

            // Assert that the loading indicator is displayed
            composeTestRule.onNodeWithText("Hold tight, it's Loading!").assertIsDisplayed()
        }
    }

    @Test
    fun successState_displaysUserData() {
        runBlocking {
            // Trigger the success state
            fakeRepository.setTestScenario(FakeRepository.TestScenario.Success)

            composeTestRule.setContent {
                val navController = rememberNavController()
                navigatorMock = NavigatorMock(navController)
                MainScreen(viewModel = viewModel, navigator = navigatorMock)
            }

            // Delay to resolve test flakiness caused by instant loading not allowing state change
            delay(150)
            // Assert that the fields are displayed as expected
            composeTestRule.onNodeWithText("Name: Test User").assertIsDisplayed()
            composeTestRule.onNodeWithText("Contributed 10 challenges").assertIsDisplayed()
            composeTestRule.onNodeWithText("Completed 20 challenges").assertIsDisplayed()
        }
    }

    @Test
    fun errorState_displaysErrorMessage() {
        runBlocking {
            // Trigger the error state
            fakeRepository.setTestScenario(FakeRepository.TestScenario.Error)
            fakeNetworkChecker.setNetworkAvailability(false)
            composeTestRule.setContent {
                val navController = rememberNavController()
                navigatorMock = NavigatorMock(navController)
                MainScreen(viewModel = viewModel, navigator = navigatorMock)
            }

            // Delay to resolve test flakiness caused by instant loading not allowing state change
            delay(150)

            // Assert that the fields are displayed as expected
            composeTestRule.onNodeWithText("Something to check, it will reload soon")
                .assertIsDisplayed()
        }
    }

    @Test
    fun bannerIsDisplayed_whenNoInternet() {
        runBlocking {
            // Trigger the loading localData state
            fakeRepository.setTestScenario(FakeRepository.TestScenario.LocalData)
            // Set no internet
            fakeNetworkChecker.setNetworkAvailability(false)

            composeTestRule.setContent {
                val navController = rememberNavController()
                navigatorMock = NavigatorMock(navController)
                MainScreen(viewModel = viewModel, navigator = navigatorMock)
            }

            // Delay to resolve test flakiness caused by instant loading not allowing state change
            delay(150)

            // Assert that the banner is displayed in the UI
            composeTestRule.onNodeWithText("Local data powered! check the internet")
                .assertIsDisplayed()
        }
    }

    @Test
    fun pullToRefresh_refreshesScreen() {
        runBlocking {
            // Trigger the success state initially
            fakeRepository.setTestScenario(FakeRepository.TestScenario.Success)

            composeTestRule.setContent {
                val navController = rememberNavController()
                navigatorMock = NavigatorMock(navController)
                MainScreen(viewModel = viewModel, navigator = navigatorMock)
            }

            // Delay to resolve test flakiness caused by instant loading not allowing state change
            delay(150)

            composeTestRule.onNodeWithText("Contributed 10 challenges").assertIsDisplayed()

            // Trigger the loading localData state, caused by internet being off
            fakeNetworkChecker.setNetworkAvailability(false)

            // Perform the pull-to-refresh action
            composeTestRule.onNodeWithTag("pullRefresh").performTouchInput {
                swipe(
                    start = Offset(0f, 200f), end = Offset(0f, 800f), durationMillis = 300
                )
            }

            // Assert that the banner is displayed in the UI
            composeTestRule.onNodeWithText("Local data powered! check the internet")
                .assertIsDisplayed()
        }
    }
}
