package com.jc.steamachievementschecker.core

class GetMyAchievementsUseCase(
    private val fetchAchievementsOnlineUseCase: FetchAchievementsOnlineUseCase,
    private val gameInfoRepository: GameInfoRepository,
    private val sortGameInfoUseCase: SortGameInfoUseCase
) {

    suspend operator fun invoke(): List<GameInfoItem> {
        val gameInfo = if (gameInfoRepository.hasOfflineDataAvailable()) {
            gameInfoRepository.getAllGameInfo()
        } else {
            fetchAchievementsOnlineUseCase()
        }
        return sortGameInfoUseCase(gameInfo)
    }
}
