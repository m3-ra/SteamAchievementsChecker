package com.jc.steamachievementschecker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameInfoDao {

    @Query("SELECT * FROM GameInfoDbEntity")
    suspend fun getAll(): List<GameInfoDbEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<GameInfoDbEntity>)
}
