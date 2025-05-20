package com.example.steamachievementschecker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.steamachievementschecker.presentation.achievementslist.AchievementsListScreen
import com.example.steamachievementschecker.presentation.theme.SteamAchievementsCheckerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SteamAchievementsCheckerTheme {
                AchievementsListScreen()
            }
        }
    }
}
