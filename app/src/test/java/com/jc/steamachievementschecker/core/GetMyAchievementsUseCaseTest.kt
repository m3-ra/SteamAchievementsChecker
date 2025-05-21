package com.jc.steamachievementschecker.core

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMyAchievementsUseCaseTest {

    private val achievementsRepository: AchievementsRepository = mockk()
    private val useCase = GetMyAchievementsUseCase(achievementsRepository)

    @Test
    fun `SHOULD sort games by achievements percentage then by name WHEN getting games info`() =
        runTest {
            // Arrange
            coEvery {
                achievementsRepository.getMyGames()
            } returns listOf(
                Game(1, "Game xyz"),
                Game(2, "Game abc"),
                Game(3, "Game def")
            )

            coEvery { achievementsRepository.getAchievementsPercentageByGame(1) } returns 50
            coEvery { achievementsRepository.getAchievementsPercentageByGame(2) } returns 100
            coEvery { achievementsRepository.getAchievementsPercentageByGame(3) } returns 50

            // Act
            val result = useCase()

            // Assert
            val expected = listOf(
                GameInfo(2, "Game abc", 100),
                GameInfo(3, "Game def", 50),
                GameInfo(1, "Game xyz", 50)
            )
            assertEquals(expected, result)
        }
}
