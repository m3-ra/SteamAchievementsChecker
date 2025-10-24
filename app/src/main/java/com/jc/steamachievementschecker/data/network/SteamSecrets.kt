package com.jc.steamachievementschecker.data.network

import com.jc.steamachievementschecker.BuildConfig

object SteamSecrets {

    val API_KEY: String = BuildConfig.STEAM_API_KEY
    val USER_ID: String = BuildConfig.STEAM_USER_ID

    init {
        require(API_KEY.isNotBlank()) { "Steam API key not configured in local.properties" }
        require(USER_ID.isNotBlank()) { "Steam User ID not configured in local.properties" }
    }
}
