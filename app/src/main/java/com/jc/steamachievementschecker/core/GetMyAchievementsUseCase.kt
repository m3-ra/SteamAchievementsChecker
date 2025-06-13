package com.jc.steamachievementschecker.core

class GetMyAchievementsUseCase(
    private val achievementsRepository: AchievementsRepository,
    private val gameInfoRepository: GameInfoRepository
) {

    suspend operator fun invoke(): List<GameInfo> =
        if (gameInfoRepository.hasOfflineDataAvailable()) {
            gameInfoRepository.getAllGameInfo()
        } else {
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
}
