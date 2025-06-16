package com.jc.steamachievementschecker.core

class GetMyAchievementsUseCase(
    private val fetchAchievementsOnlineUseCase: FetchAchievementsOnlineUseCase,
    private val gameInfoRepository: GameInfoRepository
) {

    suspend operator fun invoke(): List<GameInfo> {
        val gameInfo = if (gameInfoRepository.hasOfflineDataAvailable()) {
            gameInfoRepository.getAllGameInfo()
        } else {
            fetchAchievementsOnlineUseCase()
        }
        return gameInfo.sortedWith(
            compareByDescending<GameInfo> { it.achievementsPercentage }.thenBy { it.name }
        )
    }
}
