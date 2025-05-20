package com.jc.steamachievementschecker.data

import com.jc.steamachievementschecker.core.AchievementsRepository

class SteamAchievementsRepository(
    private val steamApi: SteamApi
) : AchievementsRepository {

    override suspend fun getMyGames() {
        val gamesResult = steamApi.getMyGames()

        val achResult = steamApi.getAchievementsByGame(2842040)
        val ach = achResult.playerstats.achievements
        val percentage = ach.filter { it.achieved == 1 }.size.toFloat() / ach.size.toFloat()
    }
}
