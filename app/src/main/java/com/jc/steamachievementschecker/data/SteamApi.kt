package com.jc.steamachievementschecker.data

import com.jc.steamachievementschecker.data.SteamSecrets.API_KEY
import com.jc.steamachievementschecker.data.SteamSecrets.USER_ID
import retrofit2.http.GET
import retrofit2.http.Query

interface SteamApi {

    @GET("/IPlayerService/GetOwnedGames/v0001/?key=$API_KEY&steamid=$USER_ID&format=json&include_appinfo=true")
    suspend fun getMyGames(): MyGamesApiEntity

    @GET("/ISteamUserStats/GetPlayerAchievements/v0001/?&key=$API_KEY&steamid=$USER_ID")
    suspend fun getAchievementsByGame(
        @Query("appid") appId: Int,
    ): PlayerStatsResponseApiEntity
}
