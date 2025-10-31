package com.jc.steamachievementschecker.core

class SortGameInfoUseCase(
    private val computeShortNameUseCase: ComputeShortNameUseCase
) {

    // TODO find a better way to store those "naming" rules in case there are more of them
    operator fun invoke(games: List<GameInfo>): List<GameInfoItem> {
        val gamesWithDisplayName = games.map { gameInfo ->
            GameInfoItem(
                id = gameInfo.id,
                name = gameInfo.name,
                achievementsResult = gameInfo.achievementsResult,
                displayName = gameInfo.name.removePrefix("The "),
                shortName = computeShortNameUseCase(gameInfo.name.trim())
            )
        }

        return gamesWithDisplayName.sortedWith(
            compareBy<GameInfoItem> {
                it.achievementsResult is AchievementsResult.NoAchievements
            }.thenByDescending {
                when (val result = it.achievementsResult) {
                    is AchievementsResult.HasAchievements -> result.percentage
                    is AchievementsResult.NoAchievements -> 0
                }
            }.thenBy(String.CASE_INSENSITIVE_ORDER) { it.displayName }
        )
    }
}
