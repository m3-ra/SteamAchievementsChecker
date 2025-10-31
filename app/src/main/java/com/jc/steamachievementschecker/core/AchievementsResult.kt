package com.jc.steamachievementschecker.core

sealed class AchievementsResult {
    data class HasAchievements(
        val percentage: Int
    ) : AchievementsResult()
    data object NoAchievements : AchievementsResult()
}
