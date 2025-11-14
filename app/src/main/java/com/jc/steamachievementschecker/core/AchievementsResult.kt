package com.jc.steamachievementschecker.core

sealed class AchievementsResult {
    data class HasAchievements(
        val percentage: Double,
        val unlockedCount: Int,
        val totalCount: Int
    ) : AchievementsResult() {
        val displayPercentage: Int
            get() = percentage.toInt()
    }
    data object NoAchievements : AchievementsResult()
}
