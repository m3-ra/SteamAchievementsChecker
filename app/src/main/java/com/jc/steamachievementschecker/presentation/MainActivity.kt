package com.jc.steamachievementschecker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListScreen
import com.jc.steamachievementschecker.presentation.theme.SteamAchievementsCheckerTheme

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
