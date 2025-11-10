package com.jc.steamachievementschecker.data.network

import com.jc.steamachievementschecker.core.AchievementsRepository
import com.jc.steamachievementschecker.core.AchievementsResult
import com.jc.steamachievementschecker.core.Game
import kotlinx.coroutines.delay
import timber.log.Timber
import java.io.IOException
import kotlin.math.pow

private const val MAX_RETRIES = 3

class SteamAchievementsRepository(
    private val steamApi: SteamApi
) : AchievementsRepository {

    override suspend fun getMyGames(): List<Game> =
        steamApi
            .getMyGames()
            .response
            .games
            .map { Game(it.appid, it.name) }

    override suspend fun getAchievementsPercentageByGame(appId: Int): AchievementsResult =
        getAchievementsPercentageByGameWithRetry(appId, maxRetries = MAX_RETRIES)

    private suspend fun getAchievementsPercentageByGameWithRetry(
        appId: Int,
        maxRetries: Int,
        currentAttempt: Int = 0
    ): AchievementsResult {
        try {
            val callResult = steamApi.getAchievementsByGame(appId)

            return when (val playerStats = callResult.playerstats) {
                is PlayerStatsApiEntity.Success -> {
                    val allAchievements = playerStats.achievements

                    if (allAchievements.isEmpty()) {
                        Timber.w("Game $appId has no achievements")
                        AchievementsResult.NoAchievements
                    } else {
                        val unlockedCount = allAchievements.count { it.achieved == 1 }
                        val totalCount = allAchievements.size
                        val percentage = ((unlockedCount.toDouble() / totalCount) * 100).toInt()
                        AchievementsResult.HasAchievements(
                            percentage = percentage,
                            unlockedCount = unlockedCount,
                            totalCount = totalCount
                        )
                    }
                }
                is PlayerStatsApiEntity.Error -> {
                    Timber.w("Game $appId returned error: ${playerStats.error}")
                    AchievementsResult.NoAchievements
                }
            }
        } catch (ex: IOException) {
            if (currentAttempt < maxRetries) {
                val delayMs = (2.0.pow(currentAttempt) * 1000).toLong()
                delay(delayMs)
                return getAchievementsPercentageByGameWithRetry(
                    appId = appId,
                    maxRetries = maxRetries,
                    currentAttempt = currentAttempt + 1
                )
            } else {
                val msg = "Failed to fetch achievements for game $appId after $maxRetries retries"
                Timber.e(ex, msg)
                return AchievementsResult.NoAchievements
            }
        } catch (t: Throwable) {
            Timber.e(t, "Unexpected error fetching achievements for game $appId")
            return AchievementsResult.NoAchievements
        }
    }
}
