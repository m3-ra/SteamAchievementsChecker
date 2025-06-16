package com.jc.steamachievementschecker.core

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ForceRefreshMyAchievementsUseCaseTest {

    private val fetchAchievementsOnlineUseCase: FetchAchievementsOnlineUseCase = mockk()
    private val useCase = ForceRefreshMyAchievementsUseCase(fetchAchievementsOnlineUseCase)

    @Test
    fun `SHOULD fetch game info online WHEN force refreshing`() =
        runTest {
            // Arrange
            val games: List<GameInfo> = listOf(mockk())
            coEvery { fetchAchievementsOnlineUseCase() } returns games

            // Act
            useCase()

            // Assert
            coVerify { fetchAchievementsOnlineUseCase() }
        }
}
