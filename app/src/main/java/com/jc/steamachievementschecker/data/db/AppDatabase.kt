package com.jc.steamachievementschecker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GameInfoDbEntity::class],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameInfoDao(): GameInfoDao
}
