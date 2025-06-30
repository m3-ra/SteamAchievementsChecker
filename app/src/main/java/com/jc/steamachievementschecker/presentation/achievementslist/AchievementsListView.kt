package com.jc.steamachievementschecker.presentation.achievementslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jc.steamachievementschecker.core.GameInfoItem
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel.AchievementsListUiState
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel.AchievementsListUiState.Loading
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel.AchievementsListUiState.Success
import com.jc.steamachievementschecker.presentation.achievementslist.GameInfoDisplay.GRID
import com.jc.steamachievementschecker.presentation.achievementslist.GameInfoDisplay.LIST
import com.jc.steamachievementschecker.presentation.theme.SteamAchievementsCheckerTheme

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
                    Text(
                        "Games achievements %",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
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
        Text(text = "${game.name} - ${game.achievementsPercentage}%")
    }
    HorizontalDivider()
}

@Composable
private fun GameGridItem(game: GameInfoItem) {
    // TODO lots to improve here
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = game.shortName)
        Text(text = "${game.achievementsPercentage}%")
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
            uiState = Success(previewGames, average = 66),
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true)
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

@Preview(showBackground = true)
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
    GameInfoItem(2, "Game abc", 100, displayName = "abc", "a"),
    GameInfoItem(3, "Game def", 50, displayName = "def", "d"),
    GameInfoItem(1, "Game xyz", 50, displayName = "xyz", "x")
)
