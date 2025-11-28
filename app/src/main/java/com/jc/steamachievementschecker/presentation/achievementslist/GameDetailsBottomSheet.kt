package com.jc.steamachievementschecker.presentation.achievementslist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jc.steamachievementschecker.core.AchievementsResult
import com.jc.steamachievementschecker.core.GameInfoItem

@Composable
internal fun GameDetailsBottomSheet(game: GameInfoItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = game.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        when (val result = game.achievementsResult) {
            is AchievementsResult.HasAchievements -> {
                Text(
                    text = "Achievement Progress: ${"%.2f".format(result.percentage)}%",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "${result.unlockedCount}/${result.totalCount} achievements completed",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            is AchievementsResult.NoAchievements -> {
                Text(
                    text = "This game has no achievements",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Spacer(modifier = Modifier.padding(bottom = 32.dp))
    }
}
