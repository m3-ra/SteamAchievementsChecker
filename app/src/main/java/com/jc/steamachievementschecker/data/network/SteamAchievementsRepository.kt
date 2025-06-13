package com.jc.steamachievementschecker.data.network

import com.jc.steamachievementschecker.core.AchievementsRepository
import com.jc.steamachievementschecker.core.Game
import java.io.IOException

class SteamAchievementsRepository(
    private val steamApi: SteamApi
) : AchievementsRepository {

    override suspend fun getMyGames(): List<Game> =
        steamApi
            .getMyGames()
            .response
            .games
            .map { Game(it.appid, it.name) }

    override suspend fun getAchievementsPercentageByGame(appId: Int): Int {
        try {
            val callResult = steamApi.getAchievementsByGame(appId)
            val allAchievements = callResult.playerstats.achievements
            val allAchieved = allAchievements.filter { it.achieved == 1 }.size.toDouble()
            val raw = allAchieved / allAchievements.size
            return (raw * 100).toInt()
        } catch (ex: IOException) {
            return getAchievementsPercentageByGame(appId)
        } catch (t: Throwable) {
            return 0
        }
    }
}
