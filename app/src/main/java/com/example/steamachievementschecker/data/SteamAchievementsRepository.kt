package com.example.steamachievementschecker.data

import com.example.steamachievementschecker.core.AchievementsRepository

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
