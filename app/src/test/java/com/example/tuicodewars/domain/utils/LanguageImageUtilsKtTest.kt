package com.example.tuicodewars.domain.utils

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GenerateLogoUrlsForLanguagesTest {

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @Test
    fun `test known languages generate correct URLs`() {
        val languages = listOf("java", "python", "kotlin", "ruby")
        val expectedUrls = listOf(
            "https://seeklogo.com/images/J/java-logo-7F8B35BAB3-seeklogo.com.png",
            "https://seeklogo.com/images/P/python-logo-A32636CAA3-seeklogo.com.png",
            "https://seeklogo.com/images/K/kotlin-logo-30C1970B05-seeklogo.com.png",
            "https://seeklogo.com/images/R/ruby-logo-087AF79367-seeklogo.com.png"
        )

        val result = languages.generateLogoUrlsForLanguages()

        assertEquals(expectedUrls, result)
    }

    @Test
    fun `test unknown language returns fallback URL`() {
        val languages = listOf("unknownLanguage")
        val expectedUrls = listOf(
            "https://seeklogo.com/images/C/coding-logo-553EFA7061-seeklogo.com.png"
        )

        val result = languages.generateLogoUrlsForLanguages()

        assertEquals(expectedUrls, result)
    }

    @Test
    fun `test empty language list returns empty URL list`() {
        val languages = emptyList<String>()

        val result = languages.generateLogoUrlsForLanguages()

        assertEquals(emptyList<String>(), result)
    }
}
