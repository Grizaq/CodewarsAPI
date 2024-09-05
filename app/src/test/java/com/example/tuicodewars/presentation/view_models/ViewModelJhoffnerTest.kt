package com.example.tuicodewars.presentation.view_models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tuicodewars.data.model.user.CodeChallenges
import com.example.tuicodewars.data.model.user.DataJhoffner
import com.example.tuicodewars.data.model.user.LanguageDetails
import com.example.tuicodewars.data.model.user.Ranks
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
class ViewModelJhoffnerTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // Ensures LiveData runs on the main thread

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val repository =
        mockk<Repository>(relaxed = true)
    private lateinit var viewModel: ViewModelJhoffner
    private val checkNetworkUseCase: CheckNetworkUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ViewModelJhoffner(repository, checkNetworkUseCase)
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
    fun `test getItemList updates itemList`() = runTest {
        val mockData = DataJhoffner(
            id = "1",
            codeChallenges = CodeChallenges(
                totalAuthored = 5,
                totalCompleted = 10
            ),
            name = "Test Name",
            ranks = Ranks(
                languages = mapOf(
                    "Kotlin" to LanguageDetails(
                        color = "purple",
                        name = "Kotlin",
                        rank = 1,
                        score = 1000
                    ),
                    "Java" to LanguageDetails(
                        color = "white",
                        name = "Java",
                        rank = 2,
                        score = 800
                    )
                )
            )
        )

        val expectedResource = Resource.Success(mockData)

        coEvery { repository.getItemsList() } returns flowOf(expectedResource)

        viewModel.getItemList()

        val actualResource = viewModel.itemList.value
        assertResourceEquals(expectedResource, actualResource)

        // Verify repository method is called within coroutine scope
        testScope.advanceUntilIdle() // Ensure all coroutines are completed
        coVerify { repository.getItemsList() }
    }

    @Test
    fun `test getItemList handles loading state`() = runTest {
        coEvery { repository.getItemsList() } returns flowOf(Resource.Loading())

        viewModel.getItemList()

        assertResourceEquals(Resource.Loading<DataJhoffner>(), viewModel.itemList.value)

        // Verify repository method is called within coroutine scope
        testScope.advanceUntilIdle() // Ensure all coroutines are completed
        coVerify { repository.getItemsList() }
    }

    @Test
    fun `test getItemList handles error state`() = runTest {
        val errorMessage = "Test Exception"
        coEvery { repository.getItemsList() } returns flowOf(Resource.Error(errorMessage))

        viewModel.getItemList()

        assertResourceEquals(Resource.Error<DataJhoffner>(errorMessage), viewModel.itemList.value)

        // Verify repository method is called within coroutine scope
        testScope.advanceUntilIdle() // Ensure all coroutines are completed
        coVerify { repository.getItemsList() }
    }

    @Test
    fun `test refreshData when internet is available`() = runTest {
        coEvery { checkNetworkUseCase.isInternetAvailable() } returns true
        coEvery { repository.getItemsList() } returns flowOf(Resource.Success(mockk()))

        viewModel.refreshData()

        // Verify that getItemsList was called
        coVerify { repository.getItemsList() }

        // Verify that the banner is not shown and refreshing is set to false
        assertFalse(viewModel.bannerStateShow.value)
        assertFalse(viewModel.isRefreshingMain.value)

        // Ensure all coroutines are completed
        testScope.advanceUntilIdle()
    }

    @Test
    fun `test refreshData when internet is not available`() = runTest {
        coEvery { checkNetworkUseCase.isInternetAvailable() } returns false

        viewModel.refreshData()

        // Verify that the banner is shown and refreshing is set to false
        assertTrue(viewModel.bannerStateShow.value)
        assertFalse(viewModel.isRefreshingMain.value)

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
