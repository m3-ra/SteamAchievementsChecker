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
                GameInfo(
                    id = game.id,
                    name = game.name,
                    achievementsPercentage = achievementsPercentage,
                    displayName = game.name
                )
            }.also {
                gameInfoRepository.saveGameInfo(it)
            }
}
