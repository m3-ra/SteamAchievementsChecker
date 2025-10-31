package com.jc.steamachievementschecker.presentation.achievementslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jc.steamachievementschecker.core.AchievementsResult
import com.jc.steamachievementschecker.core.GameInfoItem
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel.AchievementsListUiState
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel.AchievementsListUiState.Loading
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel.AchievementsListUiState.Success
import com.jc.steamachievementschecker.presentation.achievementslist.GameInfoDisplay.GRID
import com.jc.steamachievementschecker.presentation.achievementslist.GameInfoDisplay.LIST
import com.jc.steamachievementschecker.presentation.theme.SteamAchievementsCheckerTheme
import com.jc.steamachievementschecker.presentation.theme.ThemePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AchievementsListView(
    uiState: AchievementsListUiState,
    onRefresh: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var displayType by remember { mutableStateOf(LIST) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    AchievementsTitle(uiState)
                },
                actions = {
                    IconButton(
                        onClick = {
                            displayType = when (displayType) {
                                LIST -> GRID
                                GRID -> LIST
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Change display layout"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                Loading -> Loading()
                is Success -> GamesList(
                    games = uiState.games,
                    displayType = displayType,
                    onRefresh = onRefresh,
                    isRefreshing = false
                )
            }
        }
    }
}

@Composable
private fun AchievementsTitle(uiState: AchievementsListUiState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val avg = when (uiState) {
            Loading -> null
            is Success -> uiState.average
        }
        val maxed = when (uiState) {
            Loading -> null
            is Success -> uiState.maxed
        }
        Text(
            "Games achievements %",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (avg != null && maxed != null) {
            Text(
                text = "(average = $avg%, maxed = $maxed)",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GamesList(
    games: List<GameInfoItem>,
    displayType: GameInfoDisplay,
    onRefresh: () -> Unit,
    isRefreshing: Boolean
) {
    PullToRefreshBox(
        onRefresh = onRefresh,
        isRefreshing = isRefreshing
    ) {
        when (displayType) {
            LIST -> {
                LazyColumn {
                    items(games) { game ->
                        GameListItem(game)
                    }
                }
            }
            GRID -> {
                LazyVerticalGrid(columns = GridCells.Fixed(9)) {
                    items(games.size) { index ->
                        GameGridItem(games[index])
                        Spacer(modifier = Modifier.padding(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun GameListItem(game: GameInfoItem) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        val achievementText = when (val result = game.achievementsResult) {
            is AchievementsResult.HasAchievements -> "${result.percentage}%"
            is AchievementsResult.NoAchievements -> "No achievements"
        }
        Text(text = "${game.name} - $achievementText")
    }
    HorizontalDivider()
}

@Composable
private fun GameGridItem(game: GameInfoItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        Text(
            text = game.shortName,
            style = MaterialTheme.typography.labelSmall
        )
        val achievementText = when (val result = game.achievementsResult) {
            is AchievementsResult.HasAchievements -> "${result.percentage}%"
            is AchievementsResult.NoAchievements -> "N/A"
        }
        Text(
            text = achievementText,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@ThemePreviews
@Composable
private fun AchievementsListViewLoadingPreview() {
    SteamAchievementsCheckerTheme {
        AchievementsListView(
            uiState = Loading,
            onRefresh = {}
        )
    }
}

@ThemePreviews
@Composable
private fun AchievementsListViewGamesPreview() {
    SteamAchievementsCheckerTheme {
        AchievementsListView(
            uiState = Success(previewGames, average = 66, maxed = 5),
            onRefresh = {}
        )
    }
}

@ThemePreviews
@Composable
private fun GamesListPreview() {
    SteamAchievementsCheckerTheme {
        GamesList(
            games = previewGames,
            displayType = LIST,
            onRefresh = {},
            isRefreshing = false
        )
    }
}

@ThemePreviews
@Composable
private fun GamesGridPreview() {
    SteamAchievementsCheckerTheme {
        GamesList(
            games = previewGames,
            displayType = GRID,
            onRefresh = {},
            isRefreshing = false
        )
    }
}

private enum class GameInfoDisplay { LIST, GRID }

private val previewGames: List<GameInfoItem> = listOf(
    GameInfoItem(2, "Game abc", AchievementsResult.HasAchievements(100), displayName = "abc", "a"),
    GameInfoItem(3, "Game def", AchievementsResult.HasAchievements(50), displayName = "def", "d"),
    GameInfoItem(1, "Game xyz", AchievementsResult.HasAchievements(50), displayName = "xyz", "x")
)
