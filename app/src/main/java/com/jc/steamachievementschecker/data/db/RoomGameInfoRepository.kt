package com.jc.steamachievementschecker.data.db

import com.jc.steamachievementschecker.core.GameInfo
import com.jc.steamachievementschecker.core.GameInfoRepository

class RoomGameInfoRepository(
    private val gameInfoDao: GameInfoDao
) : GameInfoRepository {

    override suspend fun hasOfflineDataAvailable(): Boolean = gameInfoDao.getAll().isNotEmpty()

    override suspend fun getAllGameInfo() = gameInfoDao.getAll().map { it.toGameInfo() }

    private fun GameInfoDbEntity.toGameInfo() =
        GameInfo(
            id = id,
            name = name,
            achievementsPercentage = achievementsPercentage
        )
}
