package com.jc.steamachievementschecker.core

class ForceRefreshMyAchievementsUseCase(
    private val fetchAchievementsOnlineUseCase: FetchAchievementsOnlineUseCase
) {

    suspend operator fun invoke(): List<GameInfo> = fetchAchievementsOnlineUseCase()
}
