package com.example.steamachievementschecker.core

interface AchievementsRepository {

    suspend fun getMyGames()
}
