package com.tajweed.ustoz

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.tajweed.ustoz.navigation.NavGraph
import com.tajweed.ustoz.ui.screens.settings.ThemeMode
import com.tajweed.ustoz.ui.theme.TajweedUstodTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var themeMode by mutableStateOf(ThemeMode.SYSTEM)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("tajweed_settings", Context.MODE_PRIVATE)
        themeMode = try {
            ThemeMode.valueOf(prefs.getString("theme", ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name)
        } catch (e: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }

        // Listen for theme preference changes
        prefs.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == "theme") {
                themeMode = try {
                    ThemeMode.valueOf(sharedPreferences.getString("theme", ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name)
                } catch (e: IllegalArgumentException) {
                    ThemeMode.SYSTEM
                }
            }
        }

        setContent {
            TajweedUstodTheme(themeMode = themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }
}
