package com.jc.steamachievementschecker.core

class GetMyAchievementsUseCase(
    private val achievementsRepository: AchievementsRepository,
    private val gameInfoRepository: GameInfoRepository
) {

    suspend operator fun invoke(): List<GameInfo> {
        val gameInfo = if (gameInfoRepository.hasOfflineDataAvailable()) {
            gameInfoRepository.getAllGameInfo()
        } else {
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
        return gameInfo.sortedWith(
            compareByDescending<GameInfo> { it.achievementsPercentage }.thenBy { it.name }
        )
    }
}
