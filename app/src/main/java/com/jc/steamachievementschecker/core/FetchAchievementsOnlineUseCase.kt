package com.jc.steamachievementschecker.core

class FetchAchievementsOnlineUseCase(
    private val achievementsRepository: AchievementsRepository,
    private val gameInfoRepository: GameInfoRepository
) {

    suspend operator fun invoke(): List<GameInfo> =
        achievementsRepository
            .getMyGames()
            .map { game ->
                val achievementsPercentage =
                    achievementsRepository.getAchievementsPercentageByGame(game.id)
                GameInfo(game.id, game.name, achievementsPercentage)
            }.also {
                gameInfoRepository.saveGameInfo(it)
            }
}
