package com.jc.steamachievementschecker.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface SteamApi {

    @GET("/IPlayerService/GetOwnedGames/v0001/?format=json&include_appinfo=true")
    suspend fun getMyGames(
        @Query("key") apiKey: String = SteamSecrets.API_KEY,
        @Query("steamid") steamId: String = SteamSecrets.USER_ID
    ): MyGamesApiEntity

    @GET("/ISteamUserStats/GetPlayerAchievements/v0001/")
    suspend fun getAchievementsByGame(
        @Query("appid") appId: Int,
        @Query("key") apiKey: String = SteamSecrets.API_KEY,
        @Query("steamid") steamId: String = SteamSecrets.USER_ID
    ): PlayerStatsResponseApiEntity
}
