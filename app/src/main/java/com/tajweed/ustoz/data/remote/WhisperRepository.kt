package com.tajweed.ustoz.data.remote

import android.content.Context
import com.tajweed.ustoz.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class WhisperRepository @Inject constructor(
    private val whisperApiService: WhisperApiService,
    @ApplicationContext private val context: Context
) {

    suspend fun transcribeAudio(audioFile: File): Result<String> {
        return try {
            val requestFile = audioFile.asRequestBody("audio/m4a".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("file", audioFile.name, requestFile)

            val modelBody = Constants.WHISPER_MODEL.toRequestBody("text/plain".toMediaTypeOrNull())
            val languageBody = Constants.AUDIO_LANGUAGE.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = whisperApiService.transcribeAudio(filePart, modelBody, languageBody)

            if (response.isSuccessful) {
                val transcription = response.body()?.text
                if (transcription != null) {
                    Result.success(transcription)
                } else {
                    Result.failure(Exception("Empty transcription response"))
                }
            } else {
                Result.failure(Exception("API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
