package com.jc.steamachievementschecker.data.network

sealed class PlayerStatsApiEntity {
    data class Success(
        val steamID: String,
        val gameName: String,
        val achievements: List<AchievementsApiEntity>
    ) : PlayerStatsApiEntity()

    data class Error(
        val error: String
    ) : PlayerStatsApiEntity()
}

data class PlayerStatsResponseApiEntity(
    val playerstats: PlayerStatsApiEntity
)

data class AchievementsApiEntity(
    val apiname: String,
    val achieved: Int,
    val unlocktime: Int
)
