package com.jc.steamachievementschecker.presentation.achievementslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jc.steamachievementschecker.core.AchievementsResult
import com.jc.steamachievementschecker.core.ForceRefreshMyAchievementsUseCase
import com.jc.steamachievementschecker.core.GameInfoItem
import com.jc.steamachievementschecker.core.GetMyAchievementsUseCase
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel.AchievementsListUiState.Loading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AchievementsListViewModel(
    private val getMyAchievementsUseCase: GetMyAchievementsUseCase,
    private val forceRefreshMyAchievementsUseCase: ForceRefreshMyAchievementsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AchievementsListUiState>(Loading)
    val uiState: StateFlow<AchievementsListUiState> = _uiState

    fun fetchMyAchievements() {
        viewModelScope.launch {
            val result = getMyAchievementsUseCase()
            _uiState.update {
                AchievementsListUiState.Success(
                    games = result,
                    average = result.computeAverage(),
                    maxed = result.computeMaxed()
                )
            }
        }
    }

    fun forceRefreshMyAchievements() {
        viewModelScope.launch {
            _uiState.update { Loading }
            val result = forceRefreshMyAchievementsUseCase()
            _uiState.update {
                AchievementsListUiState.Success(
                    games = result,
                    average = result.computeAverage(),
                    maxed = result.computeMaxed()
                )
            }
        }
    }

    private fun List<GameInfoItem>.computeAverage() =
        sumOf { gameInfoItem ->
            when (val result = gameInfoItem.achievementsResult) {
                is AchievementsResult.HasAchievements -> result.percentage
                is AchievementsResult.NoAchievements -> 0
            }
        } / size

    private fun List<GameInfoItem>.computeMaxed() =
        count { gameInfoItem ->
            when (val result = gameInfoItem.achievementsResult) {
                is AchievementsResult.HasAchievements -> result.percentage == 100
                is AchievementsResult.NoAchievements -> false
            }
        }

    sealed interface AchievementsListUiState {
        data object Loading : AchievementsListUiState
        data class Success(
            val games: List<GameInfoItem>,
            val average: Int,
            val maxed: Int
        ) : AchievementsListUiState
    }
}
