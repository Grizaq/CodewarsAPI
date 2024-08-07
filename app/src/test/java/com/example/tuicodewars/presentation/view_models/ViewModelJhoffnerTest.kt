package com.example.tuicodewars.presentation.view_models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tuicodewars.data.model.user.CodeChallenges
import com.example.tuicodewars.data.model.user.Coffeescript
import com.example.tuicodewars.data.model.user.Crystal
import com.example.tuicodewars.data.model.user.Csharp
import com.example.tuicodewars.data.model.user.DataJhoffner
import com.example.tuicodewars.data.model.user.Elixir
import com.example.tuicodewars.data.model.user.Haskell
import com.example.tuicodewars.data.model.user.Java
import com.example.tuicodewars.data.model.user.Javascript
import com.example.tuicodewars.data.model.user.Languages
import com.example.tuicodewars.data.model.user.Objc
import com.example.tuicodewars.data.model.user.Overall
import com.example.tuicodewars.data.model.user.Python
import com.example.tuicodewars.data.model.user.Ranks
import com.example.tuicodewars.data.model.user.Ruby
import com.example.tuicodewars.data.model.user.Shell
import com.example.tuicodewars.data.model.user.Sql
import com.example.tuicodewars.data.model.user.Typescript
import com.example.tuicodewars.domain.repository.Repository
import com.example.tuicodewars.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
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

    private val mockLanguages = Languages(
        coffeescript = Coffeescript(rank = -4, name = "4 kyu", color = "blue", score = 903),
        java = Java(rank = -6, name = "6 kyu", color = "yellow", score = 100),
        haskell = Haskell(rank = -6, name = "6 kyu", color = "yellow", score = 126),
        typescript = Typescript(rank = -7, name = "7 kyu", color = "white", score = 22),
        csharp = Csharp(rank = -6, name = "6 kyu", color = "yellow", score = 90),
        crystal = Crystal(rank = -8, name = "8 kyu", color = "white", score = 4),
        javascript = Javascript(rank = -3, name = "3 kyu", color = "blue", score = 2683),
        ruby = Ruby(rank = -3, name = "3 kyu", color = "blue", score = 1906),
        python = Python(rank = -5, name = "5 kyu", color = "yellow", score = 357),
        elixir = Elixir(rank = -8, name = "8 kyu", color = "white", score = 2),
        sql = Sql(rank = -6, name = "6 kyu", color = "yellow", score = 86),
        shell = Shell(rank = -8, name = "8 kyu", color = "white", score = 5),
        objc = Objc(rank = -8, name = "8 kyu", color = "white", score = 4)
    )

    private val mockOverall = Overall(
        color = "blue",
        name = "3 kyu",
        rank = -3,
        score = 2683
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ViewModelJhoffner(repository)
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
            codeChallenges = CodeChallenges(totalAuthored = 5, totalCompleted = 10),
            name = "Test Name",
            ranks = Ranks(mockLanguages, mockOverall),
            skills = listOf("Skill1", "Skill2")
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
}
