package com.example.tuicodewars.presentation.view_models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tuicodewars.data.model.authored.Authored
import com.example.tuicodewars.data.model.authored.Data
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
class ViewModelAuthoredTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // Ensures LiveData runs on the main thread

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val repository =
        mockk<Repository>(relaxed = true)
    private lateinit var viewModel: ViewModelAuthoredList
    private val checkNetworkUseCase:CheckNetworkUseCase = mockk()

    private val mockData = Authored(
        data = listOf(
            Data(
                description = "Test Description",
                id = "1",
                languages = listOf("ruby", "java"),
                name = "Test Data",
                rank = 1,
                rankName = "Beginner",
                tags = listOf("Tag1", "Tag2")
            )
        )
    )


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ViewModelAuthoredList(repository, checkNetworkUseCase)
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
                assertTrue(actual is Resource.Loading)

            else -> assertEquals(expected, actual)
        }
    }

    @Test
    fun `test getAuthoredList updates authoredList`() = runTest {
        val expectedResource = Resource.Success(mockData)

        coEvery { repository.getAuthoredList() } returns flowOf(expectedResource)

        viewModel.getAuthoredList()

        val actualResource = viewModel.authoredList.value
        assertResourceEquals(expectedResource, actualResource)

        // Verify repository method is called within coroutine scope
        testScope.advanceUntilIdle() // Ensure all coroutines are completed
        coVerify { repository.getAuthoredList() }
    }

    @Test
    fun `test getAuthoredList handles loading state`() = runTest {
        coEvery { repository.getAuthoredList() } returns flowOf(Resource.Loading())

        viewModel.getAuthoredList()

        assertResourceEquals(Resource.Loading<Authored>(), viewModel.authoredList.value)

        // Verify repository method is called within coroutine scope
        testScope.advanceUntilIdle() // Ensure all coroutines are completed
        coVerify { repository.getAuthoredList() }
    }

    @Test
    fun `test getAuthoredList handles error state`() = runTest {
        val errorMessage = "Test Exception"
        coEvery { repository.getAuthoredList() } returns flowOf(Resource.Error(errorMessage))

        viewModel.getAuthoredList()

        assertResourceEquals(Resource.Error<Authored>(errorMessage), viewModel.authoredList.value)

        // Verify repository method is called within coroutine scope
        testScope.advanceUntilIdle() // Ensure all coroutines are completed
        coVerify { repository.getAuthoredList() }
    }

    @Test
    fun `test refreshData when internet is available`() = runTest {
        // Mock network check
        coEvery { checkNetworkUseCase.isInternetAvailable() } returns true

        coEvery { repository.getAuthoredList() } returns flowOf(Resource.Success(mockData))

        // Call the function to be tested
        viewModel.refreshData()

        // Verify the repository call was made
        coVerify { repository.getAuthoredList() }

        // Assert that the banner is not shown and refreshing is set to false
        assertFalse(viewModel.bannerStateShow.value)
        assertFalse(viewModel.isRefreshingList.value)

        // Ensure all coroutines are completed
        testScope.advanceUntilIdle()
    }

    @Test
    fun `test refreshData when internet is not available`() = runTest {
        coEvery { checkNetworkUseCase.isInternetAvailable() } returns false

        viewModel.refreshData()

        // Verify that the banner is shown and refreshing is set to false
        assertTrue(viewModel.bannerStateShow.value)
        assertFalse(viewModel.isRefreshingList.value)

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

