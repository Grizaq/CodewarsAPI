package com.example.tuicodewars.domain.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tuicodewars.data.local.dao.authored.AuthoredDao
import com.example.tuicodewars.data.local.dao.challenge.ChallengeDao
import com.example.tuicodewars.data.local.dao.user.DataJhoffnerDao
import com.example.tuicodewars.data.model.authored.Authored
import com.example.tuicodewars.data.model.authored.Data
import com.example.tuicodewars.data.model.challenge.Challenge
import com.example.tuicodewars.data.model.user.CodeChallenges
import com.example.tuicodewars.data.model.user.DataJhoffner
import com.example.tuicodewars.data.model.user.LanguageDetails
import com.example.tuicodewars.data.model.user.Ranks
import com.example.tuicodewars.data.remote.API
import com.example.tuicodewars.data.repository.RepositoryJhoffner
import com.example.tuicodewars.domain.utils.NetworkChecker
import com.example.tuicodewars.domain.utils.Resource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
    private val api: API = mockk()
    private val dataJhoffnerDao: DataJhoffnerDao = mockk()
    private val authoredDao: AuthoredDao = mockk()
    private val challengeDao: ChallengeDao = mockk()
    private val networkChecker: NetworkChecker = mockk()
    private lateinit var repository: RepositoryJhoffner

    private val mockLocalData = DataJhoffner(
        id = "1",
        codeChallenges = CodeChallenges(totalAuthored = 5, totalCompleted = 10),
        name = "Test Name",
        ranks = Ranks(
            languages = mapOf(
                "Kotlin" to LanguageDetails("blue", "Kotlin", 1, 1000),
                "Java" to LanguageDetails("red", "Java", 2, 800)
            )
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository =
            RepositoryJhoffner(api, dataJhoffnerDao, authoredDao, challengeDao, networkChecker)
        coEvery { networkChecker.isInternetAvailable() } returns true
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun mockGetDataJhoffner(data: DataJhoffner?) {
        coEvery { dataJhoffnerDao.getDataJhoffner(any()) } returns data
    }

    private fun mockApiResponseForItemList(response: Response<DataJhoffner>) {
        coEvery { api.getItemList() } returns response
    }

    private fun mockApiResponseForAuthoredList(response: Response<Authored>) {
        coEvery { api.getAuthoredList() } returns response
    }

    private fun mockApiResponseForChallengeData(response: Response<Challenge>) {
        coEvery { api.getChallengeData(any()) } returns response
    }

    @Test
    fun `test getItemsList returns success`() = runTest {
        val mockData = mockLocalData
        coEvery { dataJhoffnerDao.insertDataJhoffner(any()) } just Runs
        mockGetDataJhoffner(mockData)
        mockApiResponseForItemList(Response.success(mockData))

        val flow = repository.getItemsList()
        val emissions = flow.toList()

        // Ensure there are at least two emissions from the flow
        assert(emissions.size >= 2)

        // Verify that the first emission is Resource.LocalData
        val firstEmission = emissions[0]
        assert(firstEmission is Resource.LocalData)

        // Verify that the second emission is Resource.Success and contains the expected data
        val secondEmission = emissions[1]
        assert(secondEmission is Resource.Success)
        assert((secondEmission as Resource.Success).data == mockData)
    }

    @Test
    fun `test getItemsList returns error`() = runTest {
        mockGetDataJhoffner(null)
        mockApiResponseForItemList(
            Response.error<DataJhoffner>(
                404, "Not Found".toResponseBody(null)
            )
        )

        val flow = repository.getItemsList()
        val emissions = flow.toList()

        // Ensure the first emission is Resource.Loading
        assert(emissions[0] is Resource.Loading)

        // Verify that the second emission is Resource.Error with the correct error message
        assert(emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Remote server error: 404")
    }

    @Test
    fun `test getItemsList returns empty body error`() = runTest {
        mockGetDataJhoffner(null)
        mockApiResponseForItemList(Response.success<DataJhoffner>(null))

        val flow = repository.getItemsList()
        val emissions = flow.toList()

        // Ensure the first emission is Resource.Loading
        assert(emissions[0] is Resource.Loading)

        // Verify that the second emission is Resource.Error with the correct error message for an empty body
        assert(emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Empty response body")
    }

    @Test
    fun `test getItemsList handles http exception`() = runTest {
        mockGetDataJhoffner(null)
        coEvery { api.getItemList() } throws HttpException(
            Response.error<Any>(500, "Server Error".toResponseBody(null))
        )

        val flow = repository.getItemsList()
        val emissions = flow.toList()

        // Ensure the first emission is Resource.Loading
        assert(emissions[0] is Resource.Loading)

        // Verify that the second emission is Resource.Error with the correct error message for HTTP exception
        assert(emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Network error: Server Error")
    }

    @Test
    fun `test getItemsList handles io exception`() = runTest {
        mockGetDataJhoffner(null)
        coEvery { api.getItemList() } throws IOException("Network Error")

        val flow = repository.getItemsList()
        val emissions = flow.toList()

        // Ensure the first emission is Resource.Loading
        assert(emissions[0] is Resource.Loading)

        // Verify that the second emission is Resource.Error with the correct error message for IO exception
        assert(emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Network failure: Network Error")
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
        coEvery { authoredDao.getAuthoredById() } returns flowOf(null)
        mockApiResponseForAuthoredList(Response.success(mockData))
        coEvery { authoredDao.insertAuthored(any()) } returns Unit

        val flow = repository.getAuthoredList()
        val emissions = flow.toList()

        // Ensure the first emission is Resource.Loading
        assert(emissions[0] is Resource.Loading)

        // Verify that the second emission is Resource.Success and contains the expected data
        assert(emissions[1] is Resource.Success && (emissions[1] as Resource.Success).data == mockData)
    }

    @Test
    fun `test getAuthoredList returns error`() = runTest {
        coEvery { authoredDao.getAuthoredById() } returns flowOf()
        mockApiResponseForAuthoredList(
            Response.error<Authored>(
                404, "Not Found".toResponseBody(null)
            )
        )

        val flow = repository.getAuthoredList()
        val emissions = flow.toList()

        // Ensure the first emission is Resource.Loading
        assert(emissions[0] is Resource.Loading)

        // Verify that the second emission is Resource.Error with the correct error message
        assert(emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Remote server error: 404")
    }

    @Test
    fun `test getAuthoredList returns empty body error`() = runTest {
        coEvery { authoredDao.getAuthoredById() } returns flowOf()
        mockApiResponseForAuthoredList(Response.success<Authored>(null))

        val flow = repository.getAuthoredList()
        val emissions = flow.toList()

        // Ensure the first emission is Resource.Loading
        assert(emissions[0] is Resource.Loading)

        // Verify that the second emission is Resource.Error with the correct error message for an empty body
        assert(emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Empty response body")
    }

    @Test
    fun `test getAuthoredList handles http exception`() = runTest {
        coEvery { authoredDao.getAuthoredById() } returns flowOf()
        coEvery { api.getAuthoredList() } throws HttpException(
            Response.error<Any>(500, "Server Error".toResponseBody(null))
        )

        val flow = repository.getAuthoredList()
        val emissions = flow.toList()

        // Ensure the first emission is Resource.Loading
        assert(emissions[0] is Resource.Loading)

        // Verify that the second emission is Resource.Error with the correct error message for HTTP exception
        assert(emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Network error: HTTP 500 Response.error()")
    }

    @Test
    fun `test getAuthoredList handles io exception`() = runTest {
        coEvery { authoredDao.getAuthoredById() } returns flowOf()
        coEvery { api.getAuthoredList() } throws IOException("Network Error")

        val flow = repository.getAuthoredList()
        val emissions = flow.toList()

        // Ensure the first emission is Resource.Loading
        assert(emissions[0] is Resource.Loading)

        // Verify that the second emission is Resource.Error with the correct error message for IO exception
        assert(emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Network failure: Network Error")
    }

    @Test
    fun `test getChallengeData returns success`() = runTest {
        val mockData = Challenge(
            id = "5",
            description = "Test Challenge",
            languages = listOf("Kotlin", "Java"),
            name = "Challenge Name",
            totalAttempts = 10,
            totalCompleted = 5,
            url = "http://example.com"
        )
        coEvery { challengeDao.getChallengeById(any()) } returns flowOf()
        mockApiResponseForChallengeData(Response.success(mockData))
        coEvery { challengeDao.insertChallenge(any()) } returns Unit

        val flow = repository.getChallengeData("1")
        val emissions = flow.toList()

        // Ensure there is at least one emission from the flow
        assert(emissions.isNotEmpty())

        // Ensure the first emission is Resource.Loading
        assert(emissions[0] is Resource.Loading)

        // Verify that the second emission is Resource.Success and contains the expected data
        assert(emissions[1] is Resource.Success && (emissions[1] as Resource.Success).data == mockData)
    }

    @Test
    fun `test getChallengeData returns error`() = runTest {
        coEvery { challengeDao.getChallengeById(any()) } returns flowOf()
        mockApiResponseForChallengeData(
            Response.error<Challenge>(
                404, "Not Found".toResponseBody(null)
            )
        )

        val flow = repository.getChallengeData("1")
        val emissions = flow.toList()

        // Ensure there is at least one emission from the flow
        assert(emissions.isNotEmpty())

        // Ensure the first emission is Resource.Loading
        assert(emissions[0] is Resource.Loading)

        // Verify that the second emission is Resource.Error with the correct error message
        assert(emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Remote server error: 404")
    }

    @Test
    fun `test getChallengeData returns empty body error`() = runTest {
        coEvery { challengeDao.getChallengeById(any()) } returns flowOf()
        mockApiResponseForChallengeData(Response.success<Challenge>(null))

        val flow = repository.getChallengeData("1")
        val emissions = flow.toList()

        // Ensure there is at least one emission from the flow
        assert(emissions.isNotEmpty())

        // Ensure the first emission is Resource.Loading
        assert(emissions[0] is Resource.Loading)

        // Verify that the second emission is Resource.Error with the correct error message for an empty body
        assert(emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Empty response body")
    }

    @Test
    fun `test getChallengeData handles http exception`() = runTest {
        coEvery { challengeDao.getChallengeById(any()) } returns flowOf()
        coEvery { api.getChallengeData(any()) } throws HttpException(
            Response.error<Any>(500, "Server Error".toResponseBody(null))
        )

        val flow = repository.getChallengeData("1")
        val emissions = flow.toList()

        // Ensure there is at least one emission from the flow
        assert(emissions.isNotEmpty())

        // Ensure the first emission is Resource.Loading
        assert(emissions[0] is Resource.Loading)

        // Verify that the second emission is Resource.Error with the correct error message for HTTP exception
        assert(emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Network error: HTTP 500 Response.error()")
    }

    @Test
    fun `test getChallengeData handles io exception`() = runTest {
        coEvery { challengeDao.getChallengeById(any()) } returns flowOf()
        coEvery { api.getChallengeData(any()) } throws IOException("Network Error")

        val flow = repository.getChallengeData("1")
        val emissions = flow.toList()

        // Ensure there is at least one emission from the flow
        assert(emissions.isNotEmpty())

        // Ensure the first emission is Resource.Loading
        assert(emissions[0] is Resource.Loading)

        // Verify that the second emission is Resource.Error with the correct error message for IO exception
        assert(emissions[1] is Resource.Error && (emissions[1] as Resource.Error).message == "Network failure: Network Error")
    }
}