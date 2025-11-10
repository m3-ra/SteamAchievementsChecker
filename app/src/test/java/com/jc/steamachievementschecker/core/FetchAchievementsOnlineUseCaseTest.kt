package com.jc.steamachievementschecker.core

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FetchAchievementsOnlineUseCaseTest {

    private val achievementsRepository: AchievementsRepository = mockk()
    private val gameInfoRepository: GameInfoRepository = mockk()

    private val useCase = FetchAchievementsOnlineUseCase(
        achievementsRepository = achievementsRepository,
        gameInfoRepository = gameInfoRepository
    )

    @Test
    fun `SHOULD fetch online then add achievements percentages WHEN getting games info`() =
        runTest {
            // Arrange
            coEvery { gameInfoRepository.saveGameInfo(any()) } just Runs
            coEvery {
                achievementsRepository.getMyGames()
            } returns listOf(
                Game(1, "Game xyz"),
                Game(2, "Game abc"),
                Game(3, "Game def")
            )

            coEvery {
                achievementsRepository.getAchievementsPercentageByGame(1)
            } returns AchievementsResult.HasAchievements(50, 10, 20)
            coEvery {
                achievementsRepository.getAchievementsPercentageByGame(2)
            } returns AchievementsResult.HasAchievements(100, 20, 20)
            coEvery {
                achievementsRepository.getAchievementsPercentageByGame(3)
            } returns AchievementsResult.HasAchievements(50, 10, 20)

            // Act
            val result = useCase()

            // Assert
            val expected = listOf(
                GameInfo(1, "Game xyz", AchievementsResult.HasAchievements(50, 10, 20)),
                GameInfo(2, "Game abc", AchievementsResult.HasAchievements(100, 20, 20)),
                GameInfo(3, "Game def", AchievementsResult.HasAchievements(50, 10, 20))
            )
            assertEquals(expected, result)
        }

    @Test
    fun `SHOULD save game info to db WHEN data has been fetched`() =
        runTest {
            // Arrange
            coEvery { gameInfoRepository.saveGameInfo(any()) } just Runs
            coEvery { achievementsRepository.getMyGames() } returns listOf(Game(1, "Game xyz"))
            coEvery {
                achievementsRepository.getAchievementsPercentageByGame(1)
            } returns AchievementsResult.HasAchievements(50, 10, 20)

            // Act
            useCase()

            // Assert
            val expected = listOf(GameInfo(1, "Game xyz", AchievementsResult.HasAchievements(50, 10, 20)))
            coVerify { gameInfoRepository.saveGameInfo(expected) }
        }

    @Test
    fun `SHOULD handle mixed achievements WHEN some games have no achievements`() =
        runTest {
            // Arrange
            coEvery { gameInfoRepository.saveGameInfo(any()) } just Runs
            coEvery {
                achievementsRepository.getMyGames()
            } returns listOf(
                Game(1, "Game with achievements"),
                Game(2, "Game without achievements"),
                Game(3, "Game fully completed"),
                Game(4, "Another game without achievements"),
                Game(5, "Game partially completed")
            )

            coEvery {
                achievementsRepository.getAchievementsPercentageByGame(1)
            } returns AchievementsResult.HasAchievements(50, 10, 20)
            coEvery {
                achievementsRepository.getAchievementsPercentageByGame(2)
            } returns AchievementsResult.NoAchievements
            coEvery {
                achievementsRepository.getAchievementsPercentageByGame(3)
            } returns AchievementsResult.HasAchievements(100, 20, 20)
            coEvery {
                achievementsRepository.getAchievementsPercentageByGame(4)
            } returns AchievementsResult.NoAchievements
            coEvery {
                achievementsRepository.getAchievementsPercentageByGame(5)
            } returns AchievementsResult.HasAchievements(0, 0, 20)

            // Act
            val result = useCase()

            // Assert
            val expected = listOf(
                GameInfo(1, "Game with achievements", AchievementsResult.HasAchievements(50, 10, 20)),
                GameInfo(2, "Game without achievements", AchievementsResult.NoAchievements),
                GameInfo(3, "Game fully completed", AchievementsResult.HasAchievements(100, 20, 20)),
                GameInfo(4, "Another game without achievements", AchievementsResult.NoAchievements),
                GameInfo(5, "Game partially completed", AchievementsResult.HasAchievements(0, 0, 20))
            )
            assertEquals(expected, result)
        }
}
