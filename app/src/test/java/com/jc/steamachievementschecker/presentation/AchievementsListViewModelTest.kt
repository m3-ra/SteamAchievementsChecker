package com.jc.steamachievementschecker.presentation

import com.jc.steamachievementschecker.MainDispatcherRule
import com.jc.steamachievementschecker.core.ForceRefreshMyAchievementsUseCase
import com.jc.steamachievementschecker.core.GameInfoItem
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
    private val forceRefreshMyAchievementsUseCase: ForceRefreshMyAchievementsUseCase = mockk()

    private val viewModel = AchievementsListViewModel(
        getMyAchievementsUseCase = getMyAchievementsUseCase,
        forceRefreshMyAchievementsUseCase = forceRefreshMyAchievementsUseCase
    )

    @Test
    fun `SHOULD be in loading WHEN screen starts`() {
        assertEquals(Loading, viewModel.uiState.value)
    }

    @Test
    fun `SHOULD have success state WHEN games are fetched`() {
        // Arrange
        val games = listOf(
            GameInfoItem(2, "Game abc", 100, "abc", "a"),
            GameInfoItem(3, "Game def", 50, "def", "d"),
            GameInfoItem(1, "Game xyz", 50, "xyz", "x")
        )
        coEvery { getMyAchievementsUseCase() } returns games

        // Act
        viewModel.fetchMyAchievements()

        // Assert
        assertEquals(Success(games, 66, maxed = 1), viewModel.uiState.value)
    }

    @Test
    fun `SHOULD have success state WHEN games are force refreshed`() {
        // Arrange
        val games = listOf(
            GameInfoItem(2, "Game abc", 100, "abc", "a"),
            GameInfoItem(3, "Game def", 50, "def", "d"),
            GameInfoItem(1, "Game xyz", 50, "xyz", "x")
        )
        coEvery { forceRefreshMyAchievementsUseCase() } returns games

        // Act
        viewModel.forceRefreshMyAchievements()

        // Assert
        assertEquals(Success(games, 66, maxed = 1), viewModel.uiState.value)
    }
}
