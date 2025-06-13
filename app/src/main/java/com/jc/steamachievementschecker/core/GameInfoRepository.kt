package com.jc.steamachievementschecker.core

interface GameInfoRepository {

    suspend fun hasOfflineDataAvailable(): Boolean

    suspend fun getAllGameInfo(): List<GameInfo>

    suspend fun saveGameInfo(gameInfo: List<GameInfo>)
}
