package com.jc.steamachievementschecker.data

import com.jc.steamachievementschecker.core.Game
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

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
                playerstats = PlayerStatsApiEntity(
                    steamID = "76561198042733267",
                    gameName = "",
                    success = true,
                    achievements = listOf(
                        AchievementApiEntity(
                            apiname = "Elton McCarty", achieved = 1, unlocktime = 1560
                        ),
                        AchievementApiEntity(
                            apiname = "Rudolph Forbes", achieved = 0, unlocktime = 3737
                        )
                    )
                )
            )

            coEvery { steamApi.getAchievementsByGame(any()) } returns response

            // Act
            val result = repository.getAchievementsPercentageByGame(4190)

            // Assert
            assertEquals(50, result)
        }
}
