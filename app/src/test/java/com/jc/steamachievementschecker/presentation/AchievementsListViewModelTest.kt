package com.jc.steamachievementschecker.presentation

import com.jc.steamachievementschecker.MainDispatcherRule
import com.jc.steamachievementschecker.core.GameInfo
import com.jc.steamachievementschecker.core.GetMyAchievementsUseCase
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel.AchievementsListUiState.Loading
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel.AchievementsListUiState.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AchievementsListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getMyAchievementsUseCase: GetMyAchievementsUseCase = mockk()
    private val viewModel = AchievementsListViewModel(getMyAchievementsUseCase)

    @Test
    fun `SHOULD be in loading WHEN screen starts`() {
        assertEquals(Loading, viewModel.uiState.value)
    }

    @Test
    fun `SHOULD have success state WHEN games are fetched`() {
        // Arrange
        val games = listOf(
            GameInfo(2, "Game abc", 100),
            GameInfo(3, "Game def", 50),
            GameInfo(1, "Game xyz", 50)
        )
        coEvery { getMyAchievementsUseCase() } returns games

        // Act
        viewModel.fetchMyAchievements()

        // Assert
        assertEquals(Success(games), viewModel.uiState.value)
    }
}
