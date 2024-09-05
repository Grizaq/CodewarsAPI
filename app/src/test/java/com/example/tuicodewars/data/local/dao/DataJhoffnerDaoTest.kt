package com.example.tuicodewars.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tuicodewars.data.local.dao.user.DataJhoffnerDao
import com.example.tuicodewars.data.model.user.CodeChallenges
import com.example.tuicodewars.data.model.user.DataJhoffner
import com.example.tuicodewars.data.model.user.LanguageDetails
import com.example.tuicodewars.data.model.user.Ranks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class DataJhoffnerDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dataJhoffnerDao: DataJhoffnerDao = mockk()

    private val dataJhoffner = DataJhoffner(
        id = "1",
        codeChallenges = CodeChallenges(totalAuthored = 5, totalCompleted = 10),
        name = "Test Name",
        ranks = Ranks(
            languages = mapOf(
                "Kotlin" to LanguageDetails(
                    color = "blue", name = "Kotlin", rank = 1, score = 1000
                ), "Java" to LanguageDetails(color = "red", name = "Java", rank = 2, score = 800)
            )
        )
    )

    @Test
    fun insertAndGetDataJhoffner() = runBlocking {
        coEvery { dataJhoffnerDao.insertDataJhoffner(dataJhoffner) } returns Unit
        coEvery { dataJhoffnerDao.getDataJhoffner("1") } returns dataJhoffner

        dataJhoffnerDao.insertDataJhoffner(dataJhoffner)
        val result = dataJhoffnerDao.getDataJhoffner("1")

        // Assert that the retrieved result matches the inserted dataJhoffner object
        assertEquals(dataJhoffner, result)

        // Verify that the insertDataJhoffner method was called with the correct dataJhoffner object
        coVerify { dataJhoffnerDao.insertDataJhoffner(dataJhoffner) }

        // Verify that the getDataJhoffner method was called with the correct ID
        coVerify { dataJhoffnerDao.getDataJhoffner("1") }
    }
}