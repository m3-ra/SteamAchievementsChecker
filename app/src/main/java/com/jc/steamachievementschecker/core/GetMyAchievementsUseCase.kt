package com.jc.steamachievementschecker.core

class GetMyAchievementsUseCase(
    private val achievementsRepository: AchievementsRepository
) {

    suspend operator fun invoke(): List<GameInfo> =
        achievementsRepository
            .getMyGames()
            .map { game ->
                val achievementsPercentage =
                    achievementsRepository.getAchievementsPercentageByGame(game.id)
                GameInfo(game.id, game.name, achievementsPercentage)
            }.sortedWith(
                compareByDescending<GameInfo> { it.achievementsPercentage }.thenBy { it.name }
            )
}
