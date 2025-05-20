package com.jc.steamachievementschecker.presentation.achievementslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jc.steamachievementschecker.core.GameInfo
import com.jc.steamachievementschecker.core.GetMyAchievementsUseCase
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel.AchievementsListUiState.Loading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AchievementsListViewModel(
    private val getMyAchievementsUseCase: GetMyAchievementsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AchievementsListUiState>(Loading)
    val uiState: StateFlow<AchievementsListUiState> = _uiState

    fun fetchMyAchievements() {
        viewModelScope.launch {
            val result = getMyAchievementsUseCase()
            _uiState.update { AchievementsListUiState.Success(result) }
        }
    }

    sealed interface AchievementsListUiState {
        data object Loading : AchievementsListUiState
        data class Success(val games: List<GameInfo>) : AchievementsListUiState
    }
}
