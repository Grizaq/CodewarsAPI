package com.example.tuicodewars.presentation.main_screen

import com.example.tuicodewars.data.model.authored.Authored
import com.example.tuicodewars.data.model.challenge.Challenge
import com.example.tuicodewars.data.model.user.CodeChallenges
import com.example.tuicodewars.data.model.user.DataJhoffner
import com.example.tuicodewars.data.model.user.LanguageDetails
import com.example.tuicodewars.data.model.user.Ranks
import com.example.tuicodewars.domain.repository.Repository
import com.example.tuicodewars.domain.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository : Repository {
    private var testScenario: TestScenario = TestScenario.Success

    enum class TestScenario {
        Success, Loading, Error, LocalData
    }

    private val fakeData = DataJhoffner(
        id = "508f2708b3be0c0200000002",
        name = "Test User",
        codeChallenges = CodeChallenges(
            totalAuthored = 10, totalCompleted = 20
        ),
        ranks = Ranks(
            languages = mapOf(
                "Kotlin" to LanguageDetails(
                    color = "blue", name = "Kotlin", rank = 1, score = 100
                )
            )
        )
    )

    // Set the test scenario for the repository
    fun setTestScenario(scenario: TestScenario) {
        this.testScenario = scenario
    }

    override suspend fun getItemsList(): Flow<Resource<DataJhoffner>> = flow {
        // Delay to resolve test flakiness caused by instant loading not allowing state change
        delay(100)
        // Emit the corresponding resource based on the current test scenario
        emit(
            when (testScenario) {
                TestScenario.Success -> Resource.Success(fakeData)
                TestScenario.Loading -> Resource.Loading()
                TestScenario.Error -> Resource.Error("Something to check")
                TestScenario.LocalData -> Resource.LocalData(fakeData)
            }
        )
    }

    override suspend fun getAuthoredList(): Flow<Resource<Authored>> {
        TODO("Not yet implemented")
    }

    override suspend fun getChallengeData(id: String): Flow<Resource<Challenge>> {
        TODO("Not yet implemented")
    }
}
