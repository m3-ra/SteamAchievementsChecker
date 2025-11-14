package com.jc.steamachievementschecker.core

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetMyAchievementsUseCaseTest {

    private val fetchAchievementsOnlineUseCase: FetchAchievementsOnlineUseCase = mockk()
    private val gameInfoRepository: GameInfoRepository = mockk()
    private val sortGameInfoUseCase: SortGameInfoUseCase = mockk()

    private val useCase = GetMyAchievementsUseCase(
        fetchAchievementsOnlineUseCase = fetchAchievementsOnlineUseCase,
        gameInfoRepository = gameInfoRepository,
        sortGameInfoUseCase = sortGameInfoUseCase
    )

    @Before
    fun setUp() {
        every { sortGameInfoUseCase(any()) } answers { firstArg() }
    }

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
                GameInfo(1, "Game xyz", AchievementsResult.HasAchievements(50.0, 10, 20)),
                GameInfo(2, "Game abc", AchievementsResult.HasAchievements(100.0, 20, 20)),
                GameInfo(3, "Game def", AchievementsResult.HasAchievements(50.0, 10, 20))
            )
            val useCase = GetMyAchievementsUseCase(
                fetchAchievementsOnlineUseCase = fetchAchievementsOnlineUseCase,
                gameInfoRepository = gameInfoRepository,
                sortGameInfoUseCase = SortGameInfoUseCase(ComputeShortNameUseCase())
            )

            // Act
            val result = useCase()

            // Assert
            val expected = listOf(
                GameInfoItem(2, "Game abc", AchievementsResult.HasAchievements(100.0, 20, 20), "Game abc", "Ga"),
                GameInfoItem(3, "Game def", AchievementsResult.HasAchievements(50.0, 10, 20), "Game def", "Gd"),
                GameInfoItem(1, "Game xyz", AchievementsResult.HasAchievements(50.0, 10, 20), "Game xyz", "Gx")
            )
            assertEquals(expected, result)
        }

    @Test
    fun `SHOULD sort mixed achievements WHEN some games have no achievements`() =
        runTest {
            // Arrange
            coEvery { gameInfoRepository.hasOfflineDataAvailable() } returns true
            coEvery {
                gameInfoRepository.getAllGameInfo()
            } returns listOf(
                GameInfo(1, "Game xyz", AchievementsResult.HasAchievements(50.0, 10, 20)),
                GameInfo(2, "Game abc", AchievementsResult.NoAchievements),
                GameInfo(3, "Game def", AchievementsResult.HasAchievements(100.0, 20, 20)),
                GameInfo(4, "Another Game", AchievementsResult.NoAchievements),
                GameInfo(5, "The Game", AchievementsResult.HasAchievements(0.0, 0, 20))
            )
            val useCase = GetMyAchievementsUseCase(
                fetchAchievementsOnlineUseCase = fetchAchievementsOnlineUseCase,
                gameInfoRepository = gameInfoRepository,
                sortGameInfoUseCase = SortGameInfoUseCase(ComputeShortNameUseCase())
            )

            // Act
            val result = useCase()

            // Assert
            val expected = listOf(
                GameInfoItem(3, "Game def", AchievementsResult.HasAchievements(100.0, 20, 20), "Game def", "Gd"),
                GameInfoItem(1, "Game xyz", AchievementsResult.HasAchievements(50.0, 10, 20), "Game xyz", "Gx"),
                GameInfoItem(5, "The Game", AchievementsResult.HasAchievements(0.0, 0, 20), "Game", "TG"),
                GameInfoItem(4, "Another Game", AchievementsResult.NoAchievements, "Another Game", "AG"),
                GameInfoItem(2, "Game abc", AchievementsResult.NoAchievements, "Game abc", "Ga")
            )
            assertEquals(expected, result)
        }
}
