package com.jc.steamachievementschecker.core

class FetchAchievementsOnlineUseCase(
    private val achievementsRepository: AchievementsRepository,
    private val gameInfoRepository: GameInfoRepository
) {

    suspend operator fun invoke(): List<GameInfo> =
        achievementsRepository
            .getMyGames()
            .map { game ->
                val achievementsResult =
                    achievementsRepository.getAchievementsPercentageByGame(game.id)
                GameInfo(
                    id = game.id,
                    name = game.name,
                    achievementsResult = achievementsResult
                )
            }.also {
                gameInfoRepository.saveGameInfo(it)
            }
}
