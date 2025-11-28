package com.jc.steamachievementschecker.presentation.achievementslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jc.steamachievementschecker.core.AchievementsResult
import com.jc.steamachievementschecker.core.GameInfoItem

@Composable
internal fun GameGridItem(
    game: GameInfoItem,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 2.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = game.shortName,
            style = MaterialTheme.typography.labelSmall
        )
        val achievementText = when (val result = game.achievementsResult) {
            is AchievementsResult.HasAchievements -> "${result.displayPercentage}%"
            is AchievementsResult.NoAchievements -> "N/A"
        }
        Text(
            text = achievementText,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
