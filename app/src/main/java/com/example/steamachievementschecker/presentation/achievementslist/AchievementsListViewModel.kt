package com.example.steamachievementschecker.presentation.achievementslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.steamachievementschecker.core.GetMyAchievementsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AchievementsListViewModel(
    private val getMyAchievementsUseCase: GetMyAchievementsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AchievementsListUiState.Loading)
    val uiState: StateFlow<AchievementsListUiState> = _uiState

    fun fetchMyAchievements() {
        viewModelScope.launch {
            getMyAchievementsUseCase()
        }
    }

    sealed interface AchievementsListUiState {
        data object Loading : AchievementsListUiState
        data class Success(val data: String) : AchievementsListUiState
    }
}
