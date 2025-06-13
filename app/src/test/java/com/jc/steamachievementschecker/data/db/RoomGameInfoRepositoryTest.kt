package com.jc.steamachievementschecker.data.db

import com.jc.steamachievementschecker.core.GameInfo
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RoomGameInfoRepositoryTest {

    private val gameInfoDao: GameInfoDao = mockk()
    private val repository = RoomGameInfoRepository(gameInfoDao)

    @Test
    fun `SHOULD return true WHEN game info exists in db`() =
        runTest {
            // Arrange
            val games: List<GameInfoDbEntity> = listOf(mockk())
            coEvery { gameInfoDao.getAll() } returns games

            // Act
            val result = repository.hasOfflineDataAvailable()

            // Assert
            assertTrue(result)
        }

    @Test
    fun `SHOULD convert to game info WHEN fetching db data`() =
        runTest {
            // Arrange
            val game1 = GameInfoDbEntity(1, "Game 1", 50)
            val game2 = GameInfoDbEntity(2, "Game 2", 75)
            val games = listOf(game1, game2)
            coEvery { gameInfoDao.getAll() } returns games

            // Act
            val result = repository.getAllGameInfo()

            // Assert
            val expected = listOf(
                GameInfo(1, "Game 1", 50),
                GameInfo(2, "Game 2", 75)
            )
            assertEquals(expected, result)
        }

    @Test
    fun `SHOULD convert to db entity WHEN saving game info`() =
        runTest {
            // Arrange
            coEvery { gameInfoDao.insertAll(any()) } just Runs
            val games = listOf(GameInfo(1, "Game 1", 50))

            // Act
            repository.saveGameInfo(games)

            // Assert
            coVerify {
                gameInfoDao.insertAll(listOf(GameInfoDbEntity(1, "Game 1", 50)))
            }
        }
}
