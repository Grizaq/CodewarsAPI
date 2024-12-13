package com.example.tuicodewars.presentation.utils

import androidx.compose.foundation.lazy.LazyListState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ScrollToTopButtonKtTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `scrollToTop scrolls to the first item`() = runTest {
        val lazyListState = mockk<LazyListState>(relaxed = true)
        coEvery { lazyListState.animateScrollToItem(any(), any()) } returns Unit

        scrollToTop(this, lazyListState)

        // Needs a waiting time, the animation doesn't happen instantly
        advanceUntilIdle()

        // Verify the function was called with the expected arguments
        coVerify { lazyListState.animateScrollToItem(0, 0) }
    }
}