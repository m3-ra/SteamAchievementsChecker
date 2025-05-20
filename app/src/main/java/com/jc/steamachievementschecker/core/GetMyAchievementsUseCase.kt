package com.jc.steamachievementschecker.core

class GetMyAchievementsUseCase(
    private val achievementsRepository: AchievementsRepository
) {

    suspend operator fun invoke() {
        achievementsRepository.getMyGames()
    }
}
