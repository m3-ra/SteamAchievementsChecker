package com.jc.steamachievementschecker.data.network

import com.jc.steamachievementschecker.core.AchievementsResult
import com.jc.steamachievementschecker.core.Game
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class SteamAchievementsRepositoryTest {

    private val steamApi: SteamApi = mockk()
    private val repository = SteamAchievementsRepository(steamApi)

    @Test
    fun `SHOULD map to a list of games WHEN getting steam api games response`() =
        runTest {
            // Arrange
            val response = MyGamesApiEntity(
                response = GameResponseApiEntity(
                    game_count = 6546,
                    games = listOf(
                        GameApiEntity(
                            appid = 4190,
                            name = "Bianca Romero",
                            playtime_forever = 2659,
                            img_icon_url = "https://duckduckgo.com/?q=cras",
                            playtime_windows_forever = 3284,
                            playtime_mac_forever = 5019,
                            playtime_linux_forever = 7342,
                            playtime_deck_forever = 5175,
                            rtime_last_played = 5068,
                            playtime_disconnected = 1480
                        )
                    )
                )
            )
            coEvery { steamApi.getMyGames() } returns response

            // Act
            val result = repository.getMyGames()

            // Assert
            val expected = listOf(Game(4190, "Bianca Romero"))
            assertEquals(expected, result)
        }

    @Test
    fun `SHOULD compute achievements percentage WHEN getting steam api achievements response`() =
        runTest {
            // Arrange
            val response = PlayerStatsResponseApiEntity(
                playerstats = PlayerStatsApiEntity.Success(
                    steamID = "76561198042733267",
                    gameName = "",
                    achievements = listOf(
                        AchievementsApiEntity(
                            apiname = "Elton McCarty",
                            achieved = 1,
                            unlocktime = 1560
                        ),
                        AchievementsApiEntity(
                            apiname = "Rudolph Forbes",
                            achieved = 0,
                            unlocktime = 3737
                        )
                    )
                )
            )

            coEvery { steamApi.getAchievementsByGame(any()) } returns response

            // Act
            val result = repository.getAchievementsPercentageByGame(4190)

            // Assert
            assertEquals(AchievementsResult.HasAchievements(50.0, 1, 2), result)
        }

    @Test
    fun `SHOULD return NoAchievements WHEN game has no achievements`() =
        runTest {
            // Arrange
            val response = PlayerStatsResponseApiEntity(
                playerstats = PlayerStatsApiEntity.Success(
                    steamID = "76561198042733267",
                    gameName = "Game With No Achievements",
                    achievements = emptyList()
                )
            )

            coEvery { steamApi.getAchievementsByGame(any()) } returns response

            // Act
            val result = repository.getAchievementsPercentageByGame(4190)

            // Assert
            assertEquals(AchievementsResult.NoAchievements, result)
        }

    @Test
    fun `SHOULD retry and eventually succeed WHEN IOException occurs then succeeds`() =
        runTest {
            // Arrange
            val successResponse = PlayerStatsResponseApiEntity(
                playerstats = PlayerStatsApiEntity.Success(
                    steamID = "76561198042733267",
                    gameName = "",
                    achievements = listOf(
                        AchievementsApiEntity(
                            apiname = "Achievement 1",
                            achieved = 1,
                            unlocktime = 1560
                        )
                    )
                )
            )

            coEvery {
                steamApi.getAchievementsByGame(any())
            } throws IOException("Network error") andThen successResponse

            // Act
            val result = repository.getAchievementsPercentageByGame(4190)

            // Assert
            assertEquals(AchievementsResult.HasAchievements(100.0, 1, 1), result)
            coVerify(exactly = 2) { steamApi.getAchievementsByGame(4190) }
        }

    @Test
    fun `SHOULD return NoAchievements WHEN IOException persists after max retries`() =
        runTest {
            // Arrange
            coEvery {
                steamApi.getAchievementsByGame(any())
            } throws IOException("Network error")

            // Act
            val result = repository.getAchievementsPercentageByGame(4190)

            // Assert
            assertEquals(AchievementsResult.NoAchievements, result)
            coVerify(exactly = 4) { steamApi.getAchievementsByGame(4190) }
        }

    @Test
    fun `SHOULD return NoAchievements WHEN unexpected exception occurs`() =
        runTest {
            // Arrange
            coEvery {
                steamApi.getAchievementsByGame(any())
            } throws RuntimeException("Unexpected error")

            // Act
            val result = repository.getAchievementsPercentageByGame(4190)

            // Assert
            assertEquals(AchievementsResult.NoAchievements, result)
            coVerify(exactly = 1) { steamApi.getAchievementsByGame(4190) }
        }
}
