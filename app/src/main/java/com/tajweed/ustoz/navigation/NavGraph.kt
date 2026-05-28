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
import com.tajweed.ustoz.ui.screens.home.HomeScreen
import com.tajweed.ustoz.ui.screens.lessons.LessonDetailScreen
import com.tajweed.ustoz.ui.screens.lessons.LessonsListScreen
import com.tajweed.ustoz.ui.screens.practice.PracticeListScreen
import com.tajweed.ustoz.ui.screens.practice.PracticeScreen
import com.tajweed.ustoz.ui.screens.quran.QuranReaderScreen
import com.tajweed.ustoz.ui.screens.quran.QuranScreen
import com.tajweed.ustoz.ui.screens.recording.FeedbackScreen
import com.tajweed.ustoz.ui.screens.recording.RecordingScreen

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
            HomeScreen(
                onNavigateToLessons = {
                    navController.navigate(Screen.LessonsList.route)
                },
                onNavigateToQuran = {
                    navController.navigate(Screen.Quran.route)
                },
                onNavigateToPractice = {
                    navController.navigate(Screen.Practice.route)
                },
                onNavigateToLesson = { ruleId ->
                    navController.navigate(Screen.LessonDetail.createRoute(ruleId))
                }
            )
        }

        composable(Screen.LessonsList.route) {
            LessonsListScreen(
                onLessonClick = { ruleId ->
                    navController.navigate(Screen.LessonDetail.createRoute(ruleId))
                }
            )
        }

        composable(
            route = Screen.LessonDetail.route,
            arguments = listOf(navArgument("ruleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val ruleId = backStackEntry.arguments?.getInt("ruleId") ?: 0
            LessonDetailScreen(
                ruleId = ruleId,
                onBackClick = { navController.popBackStack() },
                onPracticeClick = { id ->
                    navController.navigate(Screen.PracticeSession.createRoute(id))
                }
            )
        }

        composable(Screen.Quran.route) {
            QuranScreen(
                onSurahClick = { surahNumber ->
                    navController.navigate(Screen.QuranReader.createRoute(surahNumber))
                }
            )
        }

        composable(
            route = Screen.QuranReader.route,
            arguments = listOf(navArgument("surahNumber") { type = NavType.IntType })
        ) { backStackEntry ->
            val surahNumber = backStackEntry.arguments?.getInt("surahNumber") ?: 1
            QuranReaderScreen(
                surahNumber = surahNumber,
                onBackClick = { navController.popBackStack() },
                onRecordClick = { ayahId ->
                    navController.navigate(Screen.Recording.createRoute(ayahId))
                }
            )
        }

        composable(Screen.Practice.route) {
            PracticeListScreen(navController = navController)
        }

        composable(
            route = Screen.PracticeSession.route,
            arguments = listOf(navArgument("ruleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val ruleId = backStackEntry.arguments?.getInt("ruleId") ?: 0
            PracticeScreen(
                ruleId = ruleId,
                navController = navController
            )
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
            RecordingScreen(
                ayahId = displayAyahId,
                navController = navController
            )
        }

        composable(
            route = Screen.Feedback.route,
            arguments = listOf(navArgument("resultId") { type = NavType.IntType })
        ) { backStackEntry ->
            val resultId = backStackEntry.arguments?.getInt("resultId") ?: 0
            FeedbackScreen(
                resultId = resultId,
                navController = navController
            )
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
