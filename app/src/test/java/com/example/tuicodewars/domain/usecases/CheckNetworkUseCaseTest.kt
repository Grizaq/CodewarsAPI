package com.example.tuicodewars.domain.usecases

import com.example.tuicodewars.domain.utils.NetworkChecker
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CheckNetworkUseCaseTest {

    private val networkChecker: NetworkChecker = mockk()
    private val checkNetworkUseCase = CheckNetworkUseCase(networkChecker)

    @Test
    fun `test isInternetAvailable returns true when internet is available`() {
        every { networkChecker.isInternetAvailable() } returns true

        val result = checkNetworkUseCase.isInternetAvailable()

        // Assert network checker returns true when internet available
        assertEquals(true, result)
    }

    @Test
    fun `test isInternetAvailable returns false when internet is not available`() {
        every { networkChecker.isInternetAvailable() } returns false

        val result = checkNetworkUseCase.isInternetAvailable()

        // Assert network checker returns false when internet is not available
        assertEquals(false, result)
    }
}