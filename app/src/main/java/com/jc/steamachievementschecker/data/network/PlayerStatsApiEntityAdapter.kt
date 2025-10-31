package com.jc.steamachievementschecker.data.network

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class PlayerStatsApiEntityAdapter : TypeAdapter<PlayerStatsApiEntity>() {
    override fun write(
        out: JsonWriter,
        value: PlayerStatsApiEntity?
    ): Unit = throw UnsupportedOperationException("Writing PlayerStatsApiEntity is not supported")

    override fun read(`in`: JsonReader): PlayerStatsApiEntity {
        val jsonObject = com.google.gson.JsonParser
            .parseReader(`in`)
            .asJsonObject

        return if (jsonObject.has("success") && jsonObject.get("success").asBoolean) {
            val steamID = jsonObject.get("steamID").asString
            val gameName = jsonObject.get("gameName").asString
            val achievementsArray = jsonObject.getAsJsonArray("achievements")
            val achievements = mutableListOf<AchievementsApiEntity>()

            for (element in achievementsArray) {
                val achievementObj = element.asJsonObject
                achievements.add(
                    AchievementsApiEntity(
                        apiname = achievementObj.get("apiname").asString,
                        achieved = achievementObj.get("achieved").asInt,
                        unlocktime = achievementObj.get("unlocktime").asInt
                    )
                )
            }

            PlayerStatsApiEntity.Success(
                steamID = steamID,
                gameName = gameName,
                achievements = achievements
            )
        } else {
            val error = jsonObject.get("error").asString
            PlayerStatsApiEntity.Error(error = error)
        }
    }
}

class PlayerStatsApiEntityAdapterFactory : TypeAdapterFactory {
    override fun <T : Any?> create(
        gson: Gson,
        type: TypeToken<T>
    ): TypeAdapter<T>? {
        if (type.rawType != PlayerStatsApiEntity::class.java) {
            return null
        }
        @Suppress("UNCHECKED_CAST")
        return PlayerStatsApiEntityAdapter() as TypeAdapter<T>
    }
}
