package com.example.tuicodewars.presentation.view_models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tuicodewars.data.model.challenge.Challenge
import com.example.tuicodewars.domain.repository.Repository
import com.example.tuicodewars.domain.usecases.CheckNetworkUseCase
import com.example.tuicodewars.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ViewModelChallengeDataTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // Ensures LiveData runs on the main thread

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val repository =
        mockk<Repository>(relaxed = true)
    private lateinit var viewModel: ViewModelChallengeData
    private val checkNetworkUseCase:CheckNetworkUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ViewModelChallengeData(repository, checkNetworkUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset Main dispatcher
    }

    private fun assertResourceEquals(expected: Resource<*>, actual: Resource<*>) {
        when {
            expected is Resource.Success && actual is Resource.Success -> assertEquals(
                expected.data,
                actual.data
            )

            expected is Resource.Error && actual is Resource.Error -> assertEquals(
                expected.message,
                actual.message
            )

            expected is Resource.Loading && actual is Resource.Loading ->
                // just check they are both instances of Loading
                assertTrue(actual is Resource.Loading)

            else -> assertEquals(expected, actual)
        }
    }

    @Test
    fun `test getChallengeData updates challengeData`() = runTest {
        val mockChallenge = Challenge(
            id = "4",
            description = "Test Challenge Description",
            languages = listOf("Kotlin", "Java"),
            name = "Test Challenge",
            totalAttempts = 100,
            totalCompleted = 50,
            url = "https://tui.example.com"
        )

        val expectedResource = Resource.Success(mockChallenge)
        val challengeId = "challengeId"

        coEvery { repository.getChallengeData(challengeId) } returns flowOf(expectedResource)

        viewModel.setChallengeId(challengeId)

        val actualResource = viewModel.challengeData.value
        assertResourceEquals(expectedResource, actualResource)

        // Verify repository method is called within coroutine scope
        testScope.advanceUntilIdle() // Ensure all coroutines are completed
        coVerify { repository.getChallengeData(challengeId) }
    }

    @Test
    fun `test getChallengeData handles loading state`() = runTest {
        val challengeId = "challengeId"
        coEvery { repository.getChallengeData(challengeId) } returns flowOf(Resource.Loading())

        viewModel.setChallengeId(challengeId)

        assertResourceEquals(Resource.Loading<Challenge>(), viewModel.challengeData.value)

        // Verify repository method is called within coroutine scope
        testScope.advanceUntilIdle() // Ensure all coroutines are completed
        coVerify { repository.getChallengeData(challengeId) }
    }

    @Test
    fun `test getChallengeData handles error state`() = runTest {
        val errorMessage = "Test Exception"
        val challengeId = "challengeId"
        coEvery { repository.getChallengeData(challengeId) } returns flowOf(
            Resource.Error(
                errorMessage
            )
        )

        viewModel.setChallengeId(challengeId)

        assertResourceEquals(Resource.Error<Challenge>(errorMessage), viewModel.challengeData.value)

        // Verify repository method is called within coroutine scope
        testScope.advanceUntilIdle() // Ensure all coroutines are completed
        coVerify { repository.getChallengeData(challengeId) }
    }

    @Test
    fun `test refreshData when internet is available`() = runTest {
        val challengeId = "challengeId"
        val mockChallenge = Challenge(
            id = "4",
            description = "Test Challenge Description",
            languages = listOf("Kotlin", "Java"),
            name = "Test Challenge",
            totalAttempts = 100,
            totalCompleted = 50,
            url = "https://tui.example.com"
        )
        val expectedResource = Resource.Success(mockChallenge)

        // Mock the network availability and repository response
        coEvery { checkNetworkUseCase.isInternetAvailable() } returns true
        coEvery { repository.getChallengeData(challengeId) } returns flowOf(expectedResource)

        viewModel.refreshData(challengeId)

        // Verify that the getChallengeData method was called
        coVerify { repository.getChallengeData(challengeId) }

        // Verify that the banner is not shown and refreshing is set to false
        assertFalse(viewModel.bannerStateShow.value)
        assertFalse(viewModel.isRefreshingChallenge.value)

        // Ensure all coroutines are completed
        testScope.advanceUntilIdle()
    }

    @Test
    fun `test refreshData when internet is not available`() = runTest {
        val challengeId = "challengeId"

        // Mock the network availability and repository response
        coEvery { checkNetworkUseCase.isInternetAvailable() } returns false

        viewModel.refreshData(challengeId)

        // Verify that the getChallengeData method was not called
        coVerify(exactly = 0) { repository.getChallengeData(challengeId) }

        // Verify that the banner is shown and refreshing is set to false
        assertTrue(viewModel.bannerStateShow.value)
        assertFalse(viewModel.isRefreshingChallenge.value)

        // Ensure all coroutines are completed
        testScope.advanceUntilIdle()
    }

    @Test
    fun `test hideBanner`() = runTest {
        // Set the banner to true before calling hideBanner
        viewModel._bannerStateShow.value = true

        viewModel.hideBanner()

        // Verify that the banner is hidden
        assertFalse(viewModel.bannerStateShow.value)

        // Ensure all coroutines are completed
        testScope.advanceUntilIdle()
    }
}

