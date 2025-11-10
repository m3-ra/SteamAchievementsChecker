package com.jc.steamachievementschecker.presentation.achievementslist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jc.steamachievementschecker.R
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
    var expandedGameId by remember { mutableStateOf<Int?>(null) }

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
                        val res = when (displayType) {
                            LIST -> R.drawable.ic_grid
                            GRID -> R.drawable.ic_list
                        }
                        Icon(
                            painter = painterResource(res),
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
                    expandedGameId = expandedGameId,
                    onRefresh = onRefresh,
                    isRefreshing = false,
                    onGameClick = { gameId ->
                        expandedGameId = if (expandedGameId == gameId) null else gameId
                    }
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
    expandedGameId: Int?,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    onGameClick: (Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val expandedGame = games.find { it.id == expandedGameId }

    PullToRefreshBox(
        onRefresh = onRefresh,
        isRefreshing = isRefreshing
    ) {
        when (displayType) {
            LIST -> {
                LazyColumn {
                    items(games, key = { it.id }) { game ->
                        GameListItem(
                            game = game,
                            isExpanded = expandedGameId == game.id,
                            onClick = { onGameClick(game.id) }
                        )
                    }
                }
            }
            GRID -> {
                LazyVerticalGrid(columns = GridCells.Fixed(9)) {
                    items(games.size, key = { games[it].id }) { index ->
                        GameGridItem(
                            game = games[index],
                            onClick = { onGameClick(games[index].id) }
                        )
                        Spacer(modifier = Modifier.padding(24.dp))
                    }
                }

                if (expandedGame != null) {
                    ModalBottomSheet(
                        onDismissRequest = { onGameClick(expandedGame.id) },
                        sheetState = sheetState
                    ) {
                        GameDetailsBottomSheet(game = expandedGame)
                    }
                }
            }
        }
    }
}

@Composable
private fun GameDetailsBottomSheet(game: GameInfoItem) {
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
                    text = "Achievement Progress: ${result.percentage}%",
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

@Composable
private fun GameListItem(
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
                is AchievementsResult.HasAchievements -> "${result.percentage}%"
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
                            text = "Achievement Progress: ${result.percentage}%",
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

@Composable
private fun GameGridItem(
    game: GameInfoItem,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .clickable(onClick = onClick)
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
            expandedGameId = null,
            onRefresh = {},
            isRefreshing = false,
            onGameClick = {}
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
            expandedGameId = null,
            onRefresh = {},
            isRefreshing = false,
            onGameClick = {}
        )
    }
}

private enum class GameInfoDisplay { LIST, GRID }

private val previewGames: List<GameInfoItem> = listOf(
    GameInfoItem(
        id = 2,
        name = "Game abc",
        achievementsResult = AchievementsResult.HasAchievements(100, 20, 20),
        displayName = "abc",
        shortName = "a"
    ),
    GameInfoItem(
        id = 3,
        name = "Game def",
        achievementsResult = AchievementsResult.HasAchievements(50, 10, 20),
        displayName = "def",
        shortName = "d"
    ),
    GameInfoItem(
        id = 1,
        name = "Game xyz",
        achievementsResult = AchievementsResult.HasAchievements(75, 15, 20),
        displayName = "xyz",
        shortName = "x"
    )
)
