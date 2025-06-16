package com.jc.steamachievementschecker.core

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMyAchievementsUseCaseTest {

    private val fetchAchievementsOnlineUseCase: FetchAchievementsOnlineUseCase = mockk()
    private val gameInfoRepository: GameInfoRepository = mockk()

    private val useCase = GetMyAchievementsUseCase(
        fetchAchievementsOnlineUseCase = fetchAchievementsOnlineUseCase,
        gameInfoRepository = gameInfoRepository
    )

    @Test
    fun `SHOULD fetch from db WHEN data exists locally`() =
        runTest {
            // Arrange
            coEvery { gameInfoRepository.hasOfflineDataAvailable() } returns true
            coEvery { gameInfoRepository.getAllGameInfo() } returns listOf(mockk())

            // Act
            useCase()

            // Assert
            coVerify(exactly = 1) { gameInfoRepository.getAllGameInfo() }
            coVerify(exactly = 0) { fetchAchievementsOnlineUseCase() }
        }

    @Test
    fun `SHOULD fetch online WHEN data doesn't exist locally`() =
        runTest {
            // Arrange
            coEvery { gameInfoRepository.hasOfflineDataAvailable() } returns false
            coEvery { fetchAchievementsOnlineUseCase() } returns listOf(mockk())

            // Act
            useCase()

            // Assert
            coVerify(exactly = 0) { gameInfoRepository.getAllGameInfo() }
            coVerify(exactly = 1) { fetchAchievementsOnlineUseCase() }
        }

    @Test
    fun `SHOULD apply sorting WHEN data has been fetched`() =
        runTest {
            // Arrange
            coEvery { gameInfoRepository.hasOfflineDataAvailable() } returns true
            coEvery {
                gameInfoRepository.getAllGameInfo()
            } returns listOf(
                GameInfo(1, "Game xyz", 50),
                GameInfo(2, "Game abc", 100),
                GameInfo(3, "Game def", 50)
            )

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
