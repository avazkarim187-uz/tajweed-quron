package com.tajweed.ustoz.ui.screens.settings

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.tajweed.ustoz.data.remote.FirebaseService
import com.tajweed.ustoz.data.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

enum class AppLanguage(val displayName: String) {
    UZBEK("O'zbekcha"),
    RUSSIAN("Русский"),
    ENGLISH("English")
}

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val language: AppLanguage = AppLanguage.UZBEK,
    val notificationsEnabled: Boolean = true,
    val isLoggedIn: Boolean = false,
    val userEmail: String? = null,
    val userName: String? = null,
    val apiKeyConfigured: Boolean = false,
    val showClearDataDialog: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val firebaseService: FirebaseService,
    private val progressRepository: ProgressRepository,
    private val application: Application
) : ViewModel() {

    private val prefs = application.getSharedPreferences("tajweed_settings", Context.MODE_PRIVATE)

    private val encryptedPrefs: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(application)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            application,
            "tajweed_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val theme = ThemeMode.valueOf(prefs.getString("theme", ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name)
        val language = AppLanguage.valueOf(prefs.getString("language", AppLanguage.UZBEK.name) ?: AppLanguage.UZBEK.name)
        val notifications = prefs.getBoolean("notifications", true)
        val apiKey = encryptedPrefs.getString("api_key", null)
        val currentUser = firebaseService.getCurrentUser()

        _uiState.value = SettingsUiState(
            themeMode = theme,
            language = language,
            notificationsEnabled = notifications,
            isLoggedIn = currentUser != null,
            userEmail = currentUser?.email,
            userName = currentUser?.displayName,
            apiKeyConfigured = !apiKey.isNullOrBlank()
        )
    }

    fun setTheme(theme: ThemeMode) {
        prefs.edit().putString("theme", theme.name).apply()
        _uiState.value = _uiState.value.copy(themeMode = theme)
    }

    fun setLanguage(language: AppLanguage) {
        prefs.edit().putString("language", language.name).apply()
        _uiState.value = _uiState.value.copy(language = language)
    }

    fun setNotifications(enabled: Boolean) {
        prefs.edit().putBoolean("notifications", enabled).apply()
        _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
    }

    fun showClearDataDialog() {
        _uiState.value = _uiState.value.copy(showClearDataDialog = true)
    }

    fun dismissClearDataDialog() {
        _uiState.value = _uiState.value.copy(showClearDataDialog = false)
    }

    fun clearAllData() {
        viewModelScope.launch {
            prefs.edit().clear().apply()
            _uiState.value = SettingsUiState()
        }
    }

    fun signOut() {
        firebaseService.signOut()
        _uiState.value = _uiState.value.copy(
            isLoggedIn = false,
            userEmail = null,
            userName = null
        )
    }

    fun saveApiKey(key: String) {
        encryptedPrefs.edit().putString("api_key", key).apply()
        _uiState.value = _uiState.value.copy(apiKeyConfigured = key.isNotBlank())
    }

    fun getApiKey(): String? {
        return encryptedPrefs.getString("api_key", null)
    }

    fun syncProgress() {
        viewModelScope.launch {
            val user = firebaseService.getCurrentUser() ?: return@launch
            val progressList = progressRepository.getProgress().first()
            firebaseService.syncProgress(user.uid, progressList)
        }
    }
}
