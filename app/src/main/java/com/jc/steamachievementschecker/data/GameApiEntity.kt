package com.jc.steamachievementschecker.data

data class MyGamesApiEntity(
    val response: GameResponseApiEntity
)

data class GameResponseApiEntity(
    val game_count: Int,
    val games: List<GameApiEntity>
)

data class GameApiEntity(
    val appid: Int,
    val name: String,
    val playtime_forever: Int,
    val img_icon_url: String,
    val playtime_windows_forever: Int,
    val playtime_mac_forever: Int,
    val playtime_linux_forever: Int,
    val playtime_deck_forever: Int,
    val rtime_last_played: Int,
    val playtime_disconnected: Int
)
