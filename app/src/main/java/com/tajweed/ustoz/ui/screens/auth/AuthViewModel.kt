package com.tajweed.ustoz.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.tajweed.ustoz.data.remote.FirebaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null,
    val user: FirebaseUser? = null,
    val resetPasswordSent: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseService: FirebaseService
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkAuthState()
    }

    fun checkAuthState() {
        val currentUser = firebaseService.getCurrentUser()
        _uiState.value = _uiState.value.copy(
            isAuthenticated = currentUser != null,
            user = currentUser
        )
    }

    fun signIn(email: String, password: String) {
        if (!isValidEmail(email)) {
            _uiState.value = _uiState.value.copy(error = "Email formati noto'g'ri")
            return
        }
        if (password.length < 6) {
            _uiState.value = _uiState.value.copy(error = "Parol kamida 6 belgidan iborat bo'lishi kerak")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = firebaseService.signIn(email, password)
            result.fold(
                onSuccess = { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        user = user
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Kirish xatosi yuz berdi"
                    )
                }
            )
        }
    }

    fun signUp(email: String, password: String, name: String) {
        if (name.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Ismingizni kiriting")
            return
        }
        if (!isValidEmail(email)) {
            _uiState.value = _uiState.value.copy(error = "Email formati noto'g'ri")
            return
        }
        if (password.length < 6) {
            _uiState.value = _uiState.value.copy(error = "Parol kamida 6 belgidan iborat bo'lishi kerak")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = firebaseService.signUp(email, password)
            result.fold(
                onSuccess = { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        user = user
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Ro'yxatdan o'tish xatosi yuz berdi"
                    )
                }
            )
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = FirebaseAuth.getInstance().signInWithCredential(credential).await()
                val user = authResult.user
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAuthenticated = user != null,
                    user = user
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Google bilan kirish xatosi"
                )
            }
        }
    }

    fun resetPassword(email: String) {
        if (!isValidEmail(email)) {
            _uiState.value = _uiState.value.copy(error = "Email formati noto'g'ri")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    resetPasswordSent = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Parolni tiklash xatosi"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
