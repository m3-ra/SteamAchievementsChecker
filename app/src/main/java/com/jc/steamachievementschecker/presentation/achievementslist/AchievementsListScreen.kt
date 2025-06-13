package com.jc.steamachievementschecker.presentation.achievementslist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun AchievementsListScreen(viewModel: AchievementsListViewModel = koinViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.fetchMyAchievements()
    }

    AchievementsListView(uiState)
}
