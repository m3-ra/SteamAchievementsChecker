package com.jc.steamachievementschecker.core

class ForceRefreshMyAchievementsUseCase(
    private val fetchAchievementsOnlineUseCase: FetchAchievementsOnlineUseCase,
    private val sortGameInfoUseCase: SortGameInfoUseCase
) {

    suspend operator fun invoke(): List<GameInfo> =
        sortGameInfoUseCase(
            fetchAchievementsOnlineUseCase()
        )
}
