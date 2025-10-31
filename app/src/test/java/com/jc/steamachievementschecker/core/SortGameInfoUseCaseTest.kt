package com.jc.steamachievementschecker.core

import org.junit.Assert.assertEquals
import org.junit.Test

class SortGameInfoUseCaseTest {

    private val computeShortNameUseCase = ComputeShortNameUseCase()
    val useCase = SortGameInfoUseCase(computeShortNameUseCase)

    @Test
    fun `SHOULD sort games WHEN use case is called`() {
        // Arrange
        val games = listOf(
            GameInfo(1, "Game xyz", AchievementsResult.HasAchievements(50)),
            GameInfo(2, "Game abc", AchievementsResult.HasAchievements(100)),
            GameInfo(3, "Game def", AchievementsResult.HasAchievements(50)),
            GameInfo(4, "The Game", AchievementsResult.HasAchievements(25)),
            GameInfo(5, "Lll Game", AchievementsResult.HasAchievements(25))
        )

        // Act
        val result = useCase(games)

        // Assert
        val expected = listOf(
            GameInfoItem(2, "Game abc", AchievementsResult.HasAchievements(100), "Game abc", "Ga"),
            GameInfoItem(3, "Game def", AchievementsResult.HasAchievements(50), "Game def", "Gd"),
            GameInfoItem(1, "Game xyz", AchievementsResult.HasAchievements(50), "Game xyz", "Gx"),
            GameInfoItem(4, "The Game", AchievementsResult.HasAchievements(25), "Game", "TG"),
            GameInfoItem(5, "Lll Game", AchievementsResult.HasAchievements(25), "Lll Game", "LG")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `SHOULD sort games ignoring case WHEN they have capital letters`() {
        // Arrange
        val games = listOf(
            GameInfo(1, "TU", AchievementsResult.HasAchievements(100)),
            GameInfo(2, "Ti", AchievementsResult.HasAchievements(100)),
            GameInfo(3, "Ta", AchievementsResult.HasAchievements(100)),
            GameInfo(4, "TH", AchievementsResult.HasAchievements(100))
        )

        // Act
        val result = useCase(games)

        // Assert
        val expected = listOf(
            GameInfoItem(3, "Ta", AchievementsResult.HasAchievements(100), "Ta", "Ta"),
            GameInfoItem(4, "TH", AchievementsResult.HasAchievements(100), "TH", "TH"),
            GameInfoItem(2, "Ti", AchievementsResult.HasAchievements(100), "Ti", "Ti"),
            GameInfoItem(1, "TU", AchievementsResult.HasAchievements(100), "TU", "TU")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `SHOULD sort games with mixed achievements WHEN some have no achievements`() {
        // Arrange
        val games = listOf(
            GameInfo(1, "Game xyz", AchievementsResult.HasAchievements(50)),
            GameInfo(2, "Game abc", AchievementsResult.NoAchievements),
            GameInfo(3, "Game def", AchievementsResult.HasAchievements(100)),
            GameInfo(4, "A Game", AchievementsResult.NoAchievements),
            GameInfo(5, "The Game", AchievementsResult.HasAchievements(25))
        )

        // Act
        val result = useCase(games)

        // Assert
        val expected = listOf(
            GameInfoItem(3, "Game def", AchievementsResult.HasAchievements(100), "Game def", "Gd"),
            GameInfoItem(1, "Game xyz", AchievementsResult.HasAchievements(50), "Game xyz", "Gx"),
            GameInfoItem(5, "The Game", AchievementsResult.HasAchievements(25), "Game", "TG"),
            GameInfoItem(4, "A Game", AchievementsResult.NoAchievements, "A Game", "AG"),
            GameInfoItem(2, "Game abc", AchievementsResult.NoAchievements, "Game abc", "Ga")
        )
        assertEquals(expected, result)
    }
}
