package com.tajweed.ustoz.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            PlaceholderScreen("Bosh sahifa")
        }

        composable(Screen.LessonsList.route) {
            PlaceholderScreen("Darslar ro'yxati")
        }

        composable(
            route = Screen.LessonDetail.route,
            arguments = listOf(navArgument("ruleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val ruleId = backStackEntry.arguments?.getInt("ruleId") ?: 0
            PlaceholderScreen("Dars tafsiloti: $ruleId")
        }

        composable(Screen.Quran.route) {
            PlaceholderScreen("Qur'on")
        }

        composable(
            route = Screen.QuranReader.route,
            arguments = listOf(navArgument("surahNumber") { type = NavType.IntType })
        ) { backStackEntry ->
            val surahNumber = backStackEntry.arguments?.getInt("surahNumber") ?: 1
            PlaceholderScreen("Sura: $surahNumber")
        }

        composable(Screen.Practice.route) {
            PlaceholderScreen("Mashq")
        }

        composable(
            route = Screen.PracticeSession.route,
            arguments = listOf(navArgument("ruleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val ruleId = backStackEntry.arguments?.getInt("ruleId") ?: 0
            PlaceholderScreen("Mashq sessiyasi: $ruleId")
        }

        composable(
            route = Screen.Recording.route,
            arguments = listOf(
                navArgument("ayahId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val ayahId = backStackEntry.arguments?.getInt("ayahId") ?: -1
            val displayAyahId = if (ayahId == -1) null else ayahId
            PlaceholderScreen("Yozib olish: ${displayAyahId ?: "umumiy"}")
        }

        composable(
            route = Screen.Feedback.route,
            arguments = listOf(navArgument("resultId") { type = NavType.IntType })
        ) { backStackEntry ->
            val resultId = backStackEntry.arguments?.getInt("resultId") ?: 0
            PlaceholderScreen("Natija: $resultId")
        }

        composable(Screen.Progress.route) {
            PlaceholderScreen("Jarayon")
        }

        composable(Screen.Settings.route) {
            PlaceholderScreen("Sozlamalar")
        }

        composable(Screen.Login.route) {
            PlaceholderScreen("Kirish")
        }

        composable(Screen.Register.route) {
            PlaceholderScreen("Ro'yxatdan o'tish")
        }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name)
    }
}
