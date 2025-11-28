package com.jc.steamachievementschecker.presentation.achievementslist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jc.steamachievementschecker.core.AchievementsResult
import com.jc.steamachievementschecker.core.GameInfoItem

@Composable
internal fun GameListItem(
    game: GameInfoItem,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            val achievementText = when (val result = game.achievementsResult) {
                is AchievementsResult.HasAchievements -> "${result.displayPercentage}%"
                is AchievementsResult.NoAchievements -> "No achievements"
            }
            Text(text = "${game.name} - $achievementText")
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                when (val result = game.achievementsResult) {
                    is AchievementsResult.HasAchievements -> {
                        Text(
                            text = game.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Achievement Progress: ${"%.2f".format(result.percentage)}%",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${result.unlockedCount}/${result.totalCount} achievements completed",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    is AchievementsResult.NoAchievements -> {
                        Text(
                            text = game.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "This game has no achievements",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        HorizontalDivider()
    }
}
