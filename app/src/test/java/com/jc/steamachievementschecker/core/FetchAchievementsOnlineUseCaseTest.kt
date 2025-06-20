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

            coEvery { achievementsRepository.getAchievementsPercentageByGame(1) } returns 50
            coEvery { achievementsRepository.getAchievementsPercentageByGame(2) } returns 100
            coEvery { achievementsRepository.getAchievementsPercentageByGame(3) } returns 50

            // Act
            val result = useCase()

            // Assert
            val expected = listOf(
                GameInfo(1, "Game xyz", 50, "Game xyz"),
                GameInfo(2, "Game abc", 100, "Game abc"),
                GameInfo(3, "Game def", 50, "Game def")
            )
            assertEquals(expected, result)
        }

    @Test
    fun `SHOULD save game info to db WHEN data has been fetched`() =
        runTest {
            // Arrange
            coEvery { gameInfoRepository.saveGameInfo(any()) } just Runs
            coEvery { achievementsRepository.getMyGames() } returns listOf(Game(1, "Game xyz"))
            coEvery { achievementsRepository.getAchievementsPercentageByGame(1) } returns 50

            // Act
            val result = useCase()

            // Assert
            val expected = listOf(GameInfo(1, "Game xyz", 50, "Game xyz"))
            coVerify { gameInfoRepository.saveGameInfo(expected) }
        }
}
