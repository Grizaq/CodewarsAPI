package com.example.tuicodewars.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tuicodewars.data.local.dao.authored.AuthoredDao
import com.example.tuicodewars.data.model.authored.Authored
import com.example.tuicodewars.data.model.authored.Data
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class AuthoredDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val authoredDao: AuthoredDao = mockk()

    private val authored = Authored(
        id = 1, data = listOf(
            Data(
                id = "1",
                description = "Description",
                languages = listOf("Kotlin", "Java"),
                name = "name",
                rank = 1,
                rankName = "kyu",
                tags = listOf("tag1", "tag2")
            )
        )
    )

    @Test
    fun insertAndGetAuthored() = runBlocking {
        coEvery { authoredDao.insertAuthored(any()) } returns Unit
        coEvery { authoredDao.getAuthoredById() } returns flowOf(authored)

        authoredDao.insertAuthored(authored)
        val result = authoredDao.getAuthoredById().toList().firstOrNull()

        // Assert that the retrieved result is equal to the authored object that was inserted
        assertEquals(authored, result)

        // Verify that the insertAuthored method was called with the authored object
        coVerify { authoredDao.insertAuthored(authored) }

        // Verify that the getAuthoredById method was called
        coVerify { authoredDao.getAuthoredById() }
    }

    @Test
    fun clearAuthoredTable() = runBlocking {
        coEvery { authoredDao.clearAuthoredTable() } returns Unit

        authoredDao.clearAuthoredTable()

        // Verify that the clearAuthoredTable method was called
        coVerify { authoredDao.clearAuthoredTable() }
    }
}