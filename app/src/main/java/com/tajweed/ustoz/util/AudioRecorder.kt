package com.tajweed.ustoz.util

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

class AudioRecorder(private val context: Context) {

    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: File? = null
    private var recording = false

    fun startRecording(outputFile: File) {
        this.outputFile = outputFile

        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(128000)
            setAudioSamplingRate(44100)
            setOutputFile(outputFile.absolutePath)
            prepare()
            start()
        }

        recording = true
    }

    fun stopRecording(): File {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        recording = false

        return outputFile ?: throw IllegalStateException("No output file available")
    }

    fun isRecording(): Boolean {
        return recording
    }

    fun release() {
        mediaRecorder?.release()
        mediaRecorder = null
        recording = false
    }
}
