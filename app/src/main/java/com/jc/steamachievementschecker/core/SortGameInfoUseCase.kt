package com.jc.steamachievementschecker.core

class SortGameInfoUseCase {

    operator fun invoke(games: List<GameInfo>): List<GameInfo> {
        val gamesWithDisplayName = games.map { gameInfo ->
            // TODO find a better way to store those "naming" rules in case there are more of them
            if (gameInfo.name.startsWith("The ")) {
                gameInfo.copy(displayName = gameInfo.name.removePrefix("The "))
            } else {
                gameInfo
            }
        }

        return gamesWithDisplayName.sortedWith(
            compareByDescending<GameInfo> { it.achievementsPercentage }
                .thenBy(String.CASE_INSENSITIVE_ORDER) { it.displayName }
        )
    }
}
