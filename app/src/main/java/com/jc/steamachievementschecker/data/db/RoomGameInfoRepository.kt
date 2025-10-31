package com.jc.steamachievementschecker.data.db

import com.jc.steamachievementschecker.core.AchievementsResult
import com.jc.steamachievementschecker.core.GameInfo
import com.jc.steamachievementschecker.core.GameInfoRepository

class RoomGameInfoRepository(
    private val gameInfoDao: GameInfoDao
) : GameInfoRepository {

    override suspend fun hasOfflineDataAvailable(): Boolean = gameInfoDao.getAll().isNotEmpty()

    override suspend fun getAllGameInfo() = gameInfoDao.getAll().map { it.toGameInfo() }

    override suspend fun saveGameInfo(gameInfo: List<GameInfo>) {
        gameInfoDao.insertAll(gameInfo.map { it.toGameInfoDbEntity() })
    }

    private fun GameInfoDbEntity.toGameInfo() =
        GameInfo(
            id = id,
            name = name,
            achievementsResult = if (hasNoAchievements) {
                AchievementsResult.NoAchievements
            } else {
                AchievementsResult.HasAchievements(achievementsPercentage)
            }
        )

    private fun GameInfo.toGameInfoDbEntity() =
        when (val result = achievementsResult) {
            is AchievementsResult.HasAchievements -> GameInfoDbEntity(
                id = id,
                name = name,
                achievementsPercentage = result.percentage,
                hasNoAchievements = false
            )
            AchievementsResult.NoAchievements -> GameInfoDbEntity(
                id = id,
                name = name,
                achievementsPercentage = 0,
                hasNoAchievements = true
            )
        }
}
