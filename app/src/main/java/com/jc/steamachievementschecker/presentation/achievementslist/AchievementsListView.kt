package com.jc.steamachievementschecker.presentation.achievementslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jc.steamachievementschecker.core.GameInfo
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel.AchievementsListUiState
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel.AchievementsListUiState.Loading
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel.AchievementsListUiState.Success
import com.jc.steamachievementschecker.presentation.theme.SteamAchievementsCheckerTheme

@Composable
internal fun AchievementsListView(
    uiState: AchievementsListUiState,
    onRefresh: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            Loading -> Loading()
            is Success -> GamesList(
                games = uiState.games,
                onRefresh = onRefresh,
                isRefreshing = false
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GamesList(
    games: List<GameInfo>,
    onRefresh: () -> Unit,
    isRefreshing: Boolean
) {
    PullToRefreshBox(
        onRefresh = onRefresh,
        isRefreshing = isRefreshing
    ) {
        LazyColumn {
            items(games) { game ->
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = "${game.name} - ${game.achievementsPercentage}%")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AchievementsListViewLoadingPreview() {
    SteamAchievementsCheckerTheme {
        AchievementsListView(
            uiState = Loading,
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AchievementsListViewGamesPreview() {
    SteamAchievementsCheckerTheme {
        AchievementsListView(
            uiState = Success(
                listOf(
                    GameInfo(2, "Game abc", 100),
                    GameInfo(3, "Game def", 50),
                    GameInfo(1, "Game xyz", 50)
                )
            ),
            onRefresh = {}
        )
    }
}
