package com.example.tuicodewars.domain.usecases

import com.example.tuicodewars.domain.utils.NetworkChecker
import javax.inject.Inject


class CheckNetworkUseCase @Inject constructor(
    private val networkChecker: NetworkChecker
) {
    fun isInternetAvailable(): Boolean {
        return networkChecker.isInternetAvailable()
    }
}
