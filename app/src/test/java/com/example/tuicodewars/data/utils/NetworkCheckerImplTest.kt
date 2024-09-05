package com.example.tuicodewars.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import io.mockk.*
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test

class NetworkCheckerImplTest {

    private lateinit var networkChecker: NetworkCheckerImpl
    private val context: Context = mockk()
    private val connectivityManager: ConnectivityManager = mockk()
    private val networkCapabilities: NetworkCapabilities = mockk()

    @Before
    fun setUp() {
        // Mock the Context and ConnectivityManager
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager

        networkChecker = NetworkCheckerImpl(context)
    }

    @Test
    fun `test isInternetAvailable returns true when network capabilities indicate internet`() {
        val network = mockk<android.net.Network>()
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true

        val result = networkChecker.isInternetAvailable()

        assertTrue(result)
    }

    @Test
    fun `test isInternetAvailable returns false when network capabilities do not indicate internet`() {
        val network = mockk<android.net.Network>()
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false

        val result = networkChecker.isInternetAvailable()

        assertFalse(result)
    }

    @Test
    fun `test isInternetAvailable returns false when no active network`() {
        every { connectivityManager.activeNetwork } returns null

        val result = networkChecker.isInternetAvailable()

        assertFalse(result)
    }

    @Test
    fun `test isInternetAvailable returns false when network capabilities are null`() {
        val network = mockk<android.net.Network>()
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns null

        val result = networkChecker.isInternetAvailable()

        assertFalse(result)
    }

    @After
    fun tearDown() {
        clearMocks(context, connectivityManager, networkCapabilities)
    }
}