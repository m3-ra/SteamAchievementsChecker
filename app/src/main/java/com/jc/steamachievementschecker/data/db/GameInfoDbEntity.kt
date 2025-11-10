package com.jc.steamachievementschecker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameInfoDbEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val achievementsPercentage: Int,
    val achievementsUnlocked: Int = 0,
    val achievementsTotal: Int = 0,
    val hasNoAchievements: Boolean = false
)
