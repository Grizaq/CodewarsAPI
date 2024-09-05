package com.example.tuicodewars.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tuicodewars.data.local.dao.challenge.ChallengeDao
import com.example.tuicodewars.data.model.challenge.Challenge
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class ChallengeDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val challengeDao: ChallengeDao = mockk()

    private val challenge = Challenge(
        id = "1",
        description = "Description",
        languages = listOf("Kotlin", "Java"),
        name = "name",
        totalAttempts = 10,
        totalCompleted = 5,
        url = "http://example.com"
    )

    @Test
    fun insertAndGetChallengeById() = runBlocking {
        coEvery { challengeDao.insertChallenge(any()) } returns Unit
        coEvery { challengeDao.getChallengeById("1") } returns flowOf(challenge)

        challengeDao.insertChallenge(challenge)
        val result = challengeDao.getChallengeById("1").toList().firstOrNull()

        // Verify that the result retrieved from getChallengeById is the same as the challenge object that was inserted
        assertEquals(challenge, result)

        // Verify that the insertChallenge method was called with the challenge object
        coVerify { challengeDao.insertChallenge(challenge) }

        // Verify that the getChallengeById method was called with the correct id
        coVerify { challengeDao.getChallengeById("1") }
    }

    @Test
    fun deleteChallengeById() = runBlocking {
        coEvery { challengeDao.deleteChallengeById("1") } returns Unit

        challengeDao.deleteChallengeById("1")

        // Verify that the deleteChallengeById method was called with the correct id
        coVerify { challengeDao.deleteChallengeById("1") }
    }

    @Test
    fun clearAllChallenges() = runBlocking {
        coEvery { challengeDao.clearAllChallenges() } returns Unit

        challengeDao.clearAllChallenges()

        // Verify that the clearAllChallenges method was called
        coVerify { challengeDao.clearAllChallenges() }
    }
}