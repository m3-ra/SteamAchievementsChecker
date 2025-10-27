package com.jc.steamachievementschecker.data.network

import com.jc.steamachievementschecker.core.AchievementsRepository
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

    override suspend fun getAchievementsPercentageByGame(appId: Int): Int =
        getAchievementsPercentageByGameWithRetry(appId, maxRetries = MAX_RETRIES)

    private suspend fun getAchievementsPercentageByGameWithRetry(
        appId: Int,
        maxRetries: Int,
        currentAttempt: Int = 0
    ): Int {
        try {
            val callResult = steamApi.getAchievementsByGame(appId)
            val allAchievements = callResult.playerstats.achievements

            if (allAchievements.isEmpty()) {
                Timber.w("Game $appId has no achievements")
                return 0
            }

            val allAchieved = allAchievements.count { it.achieved == 1 }.toDouble()
            val raw = allAchieved / allAchievements.size
            return (raw * 100).toInt()
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
                return 0
            }
        } catch (t: Throwable) {
            Timber.e(t, "Unexpected error fetching achievements for game $appId")
            return 0
        }
    }
}
