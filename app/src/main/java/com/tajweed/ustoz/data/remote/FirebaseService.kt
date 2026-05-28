package com.tajweed.ustoz.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.tajweed.ustoz.data.model.UserProgress
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    suspend fun syncProgress(userId: String, progress: List<UserProgress>) {
        val batch = firestore.batch()
        progress.forEach { item ->
            val docRef = firestore.collection("users")
                .document(userId)
                .collection("progress")
                .document(item.ruleId.toString())
            batch.set(docRef, mapOf(
                "ruleId" to item.ruleId,
                "lessonCompleted" to item.lessonCompleted,
                "practiceScore" to item.practiceScore,
                "lastPracticeDate" to item.lastPracticeDate,
                "totalAttempts" to item.totalAttempts,
                "successfulAttempts" to item.successfulAttempts
            ))
        }
        batch.commit().await()
    }

    suspend fun getRemoteProgress(userId: String): List<UserProgress> {
        val snapshot = firestore.collection("users")
            .document(userId)
            .collection("progress")
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            try {
                UserProgress(
                    ruleId = (doc.getLong("ruleId") ?: return@mapNotNull null).toInt(),
                    lessonCompleted = doc.getBoolean("lessonCompleted") ?: false,
                    practiceScore = (doc.getDouble("practiceScore") ?: 0.0).toFloat(),
                    lastPracticeDate = doc.getLong("lastPracticeDate") ?: 0L,
                    totalAttempts = (doc.getLong("totalAttempts") ?: 0L).toInt(),
                    successfulAttempts = (doc.getLong("successfulAttempts") ?: 0L).toInt()
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Sign in failed: user is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Sign up failed: user is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
