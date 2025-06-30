package com.jc.steamachievementschecker.core

class SortGameInfoUseCase {

    // TODO find a better way to store those "naming" rules in case there are more of them
    operator fun invoke(games: List<GameInfo>): List<GameInfoItem> {
        val gamesWithDisplayName = games.map { gameInfo ->
            GameInfoItem(
                id = gameInfo.id,
                name = gameInfo.name,
                achievementsPercentage = gameInfo.achievementsPercentage,
                displayName = gameInfo.name.removePrefix("The "),
                shortName = gameInfo.name.trim().computeShortName()
            )
        }

        return gamesWithDisplayName.sortedWith(
            compareByDescending<GameInfoItem> { it.achievementsPercentage }
                .thenBy(String.CASE_INSENSITIVE_ORDER) { it.displayName }
        )
    }

    private fun String.computeShortName(): String =
        split(" ")
            .map { it.first() }
            .joinToString("")
}
