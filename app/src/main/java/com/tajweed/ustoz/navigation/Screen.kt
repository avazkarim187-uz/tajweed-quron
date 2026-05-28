package com.tajweed.ustoz.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object LessonsList : Screen("lessons_list")
    object LessonDetail : Screen("lesson_detail/{ruleId}") {
        fun createRoute(ruleId: Int) = "lesson_detail/$ruleId"
    }
    object Quran : Screen("quran")
    object QuranReader : Screen("quran_reader/{surahNumber}") {
        fun createRoute(surahNumber: Int) = "quran_reader/$surahNumber"
    }
    object Practice : Screen("practice")
    object PracticeSession : Screen("practice_session/{ruleId}") {
        fun createRoute(ruleId: Int) = "practice_session/$ruleId"
    }
    object Recording : Screen("recording?ayahId={ayahId}") {
        fun createRoute(ayahId: Int? = null) = if (ayahId != null) {
            "recording?ayahId=$ayahId"
        } else {
            "recording"
        }
    }
    object Feedback : Screen("feedback/{resultId}") {
        fun createRoute(resultId: Int) = "feedback/$resultId"
    }
    object Progress : Screen("progress")
    object Settings : Screen("settings")
    object Login : Screen("login")
    object Register : Screen("register")
}
