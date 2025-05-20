package com.example.steamachievementschecker.data

data class PlayerStatsResponseApiEntity(
    val playerstats: PlayerStatsApiEntity
)

data class PlayerStatsApiEntity(
    val steamID: String,
    val gameName: String,
    val achievements: List<AchievementApiEntity>,
    val success: Boolean
)

data class AchievementApiEntity(
    val apiname: String,
    val achieved: Int,
    val unlocktime: Int
)
