package com.jc.steamachievementschecker.core

interface AchievementsRepository {

    suspend fun getMyGames(): List<Game>

    suspend fun getAchievementsPercentageByGame(appId: Int): Int
}
