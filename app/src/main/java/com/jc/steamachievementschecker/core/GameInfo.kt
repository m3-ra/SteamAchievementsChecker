package com.jc.steamachievementschecker.core

data class GameInfo(
    val id: Int,
    val name: String,
    val achievementsPercentage: Int,
    val displayName: String
)
