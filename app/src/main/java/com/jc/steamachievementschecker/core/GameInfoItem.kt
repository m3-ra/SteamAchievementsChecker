package com.jc.steamachievementschecker.core

data class GameInfoItem(
    val id: Int,
    val name: String,
    val achievementsPercentage: Int,
    val displayName: String,
    val shortName: String
)
