package com.jc.steamachievementschecker.core

data class GameInfoItem(
    val id: Int,
    val name: String,
    val achievementsResult: AchievementsResult,
    val displayName: String,
    val shortName: String
)
