package com.jc.steamachievementschecker.core

interface AchievementsRepository {

    suspend fun getMyGames()
}
