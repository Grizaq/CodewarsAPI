package com.example.tuicodewars.domain.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tuicodewars.data.model.authored.Authored
import com.example.tuicodewars.data.model.authored.Data
import com.example.tuicodewars.data.model.challenge.Challenge
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
import com.example.tuicodewars.data.remote.API
import com.example.tuicodewars.data.repository.RepositoryJhoffner
import com.example.tuicodewars.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class RepositoryJhoffnerTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val api = mockk<API>()
    private lateinit var repository: RepositoryJhoffner

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
        repository = RepositoryJhoffner(api)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getItemsList returns success`() = runTest {
        val mockData = DataJhoffner(
            codeChallenges = CodeChallenges(totalAuthored = 5, totalCompleted = 10),
            name = "Test Name",
            ranks = Ranks(mockLanguages, mockOverall),
            skills = listOf("Skill1", "Skill2")
        )
        val mockResponse = Response.success(mockData)

        coEvery { api.getItemList() } returns mockResponse

        val flow = repository.getItemsList()

        val emissions = flow.toList()
        assert(emissions[0] is Resource.Loading)
        assert(
            emissions[1] is Resource.Success && (emissions[1] as Resource.Success).data == mockData
        )
    }

    @Test
    fun `test getItemsList returns error`() = runTest {
        val mockResponse = Response.error<DataJhoffner>(404, "Not Found".toResponseBody(null))

        coEvery { api.getItemList() } returns mockResponse

        val flow = repository.getItemsList()

        val emissions = flow.toList()
        assert(emissions[0] is Resource.Loading)
        assert(emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "404")
    }

    @Test
    fun `test getItemsList returns empty body error`() = runTest {
        val mockResponse = Response.success<DataJhoffner>(null)

        coEvery { api.getItemList() } returns mockResponse

        val flow = repository.getItemsList()

        val emissions = flow.toList()
        assert(emissions[0] is Resource.Loading)
        assert(
            emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Empty response body"
        )
    }

    @Test
    fun `test getItemsList handles http exception`() = runTest {
        coEvery { api.getItemList() } throws HttpException(
            Response.error<Any>(
                500,
                "Server Error".toResponseBody(null)
            )
        )

        val flow = repository.getItemsList()

        val emissions = flow.toList()
        assert(emissions[0] is Resource.Loading)
        assert(
            emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Could not load data"
        )
    }

    @Test
    fun `test getItemsList handles io exception`() = runTest {
        coEvery { api.getItemList() } throws IOException("Network Error")

        val flow = repository.getItemsList()

        val emissions = flow.toList()
        assert(emissions[0] is Resource.Loading)
        assert(
            emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Check internet"
        )
    }

    @Test
    fun `test getAuthoredList returns success`() = runTest {
        val mockData = Authored(
            data = listOf(
                Data(
                    description = "Test Description",
                    id = "1",
                    languages = listOf("Kotlin", "Java"),
                    name = "Test Data",
                    rank = 1,
                    rankName = "Beginner",
                    tags = listOf("Tag1", "Tag2")
                )
            )
        )
        val mockResponse = Response.success(mockData)

        coEvery { api.getAuthoredList() } returns mockResponse

        val flow = repository.getAuthoredList()

        val emissions = flow.toList()
        assert(emissions[0] is Resource.Loading)
        assert(
            emissions[1] is Resource.Success && (emissions[1] as Resource.Success).data == mockData
        )
    }

    @Test
    fun `test getAuthoredList returns error`() = runTest {
        val mockResponse = Response.error<Authored>(404, "Not Found".toResponseBody(null))

        coEvery { api.getAuthoredList() } returns mockResponse

        val flow = repository.getAuthoredList()

        val emissions = flow.toList()
        assert(emissions[0] is Resource.Loading)
        assert(emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "404")
    }

    @Test
    fun `test getAuthoredList returns empty body error`() = runTest {
        val mockResponse = Response.success<Authored>(null)

        coEvery { api.getAuthoredList() } returns mockResponse

        val flow = repository.getAuthoredList()

        val emissions = flow.toList()
        assert(emissions[0] is Resource.Loading)
        assert(
            emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Empty response body"
        )
    }

    @Test
    fun `test getAuthoredList handles http exception`() = runTest {
        coEvery { api.getAuthoredList() } throws HttpException(
            Response.error<Any>(
                500,
                "Server Error".toResponseBody(null)
            )
        )

        val flow = repository.getAuthoredList()

        val emissions = flow.toList()
        assert(emissions[0] is Resource.Loading)
        assert(
            emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Could not load data"
        )
    }

    @Test
    fun `test getAuthoredList handles io exception`() = runTest {
        coEvery { api.getAuthoredList() } throws IOException("Network Error")

        val flow = repository.getAuthoredList()

        val emissions = flow.toList()
        assert(emissions[0] is Resource.Loading)
        assert(
            emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Check internet"
        )
    }

    @Test
    fun `test getChallengeData returns success`() = runTest {
        val mockData = Challenge(
            description = "Test Challenge",
            languages = listOf("Kotlin", "Java"),
            name = "Challenge Name",
            totalAttempts = 10,
            totalCompleted = 5,
            url = "http://example.com"
        )
        val mockResponse = Response.success(mockData)

        coEvery { api.getChallengeData(any()) } returns mockResponse

        val flow = repository.getChallengeData("1")

        val emissions = flow.toList()
        assert(emissions[0] is Resource.Loading)
        assert(
            emissions[1] is Resource.Success && (emissions[1] as Resource.Success).data == mockData
        )
    }

    @Test
    fun `test getChallengeData returns error`() = runTest {
        val mockResponse = Response.error<Challenge>(404, "Not Found".toResponseBody(null))

        coEvery { api.getChallengeData(any()) } returns mockResponse

        val flow = repository.getChallengeData("1")

        val emissions = flow.toList()
        assert(emissions[0] is Resource.Loading)
        assert(emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "404")
    }

    @Test
    fun `test getChallengeData returns empty body error`() = runTest {
        val mockResponse = Response.success<Challenge>(null)

        coEvery { api.getChallengeData(any()) } returns mockResponse

        val flow = repository.getChallengeData("1")

        val emissions = flow.toList()
        assert(emissions[0] is Resource.Loading)
        assert(
            emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Empty response body"
        )
    }

    @Test
    fun `test getChallengeData handles http exception`() = runTest {
        coEvery { api.getChallengeData(any()) } throws HttpException(
            Response.error<Any>(
                500,
                "Server Error".toResponseBody(null)
            )
        )

        val flow = repository.getChallengeData("1")

        val emissions = flow.toList()
        assert(emissions[0] is Resource.Loading)
        assert(
            emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Could not load data"
        )
    }

    @Test
    fun `test getChallengeData handles io exception`() = runTest {
        coEvery { api.getChallengeData(any()) } throws IOException("Network Error")

        val flow = repository.getChallengeData("1")

        val emissions = flow.toList()
        assert(emissions[0] is Resource.Loading)
        assert(
            emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Check internet"
        )
    }
}
