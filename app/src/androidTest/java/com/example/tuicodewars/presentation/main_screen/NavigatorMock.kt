package com.example.tuicodewars.presentation.main_screen

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class NavigatorMock(private val navController: NavHostController) : DestinationsNavigator {
    override fun clearBackStack(route: String): Boolean {
        return true
    }

    override fun navigate(
        route: String,
        onlyIfResumed: Boolean,
        builder: NavOptionsBuilder.() -> Unit
    ) {
        // Create a NavOptionsBuilder instance and navigate directly
        navController.navigate(route)
    }

    override fun navigateUp(): Boolean {
        return true
    }

    override fun popBackStack(): Boolean {
        return true
    }

    override fun popBackStack(route: String, inclusive: Boolean, saveState: Boolean): Boolean {
        return true
    }
}
