package com.example.tuicodewars.presentation.main_screen

import com.example.tuicodewars.domain.utils.NetworkChecker

class FakeNetworkChecker : NetworkChecker {
    // Default to true for testing
    private var isAvailable: Boolean = true

    fun setNetworkAvailability(available: Boolean) {
        isAvailable = available
    }

    override fun isInternetAvailable(): Boolean {
        return isAvailable
    }
}
