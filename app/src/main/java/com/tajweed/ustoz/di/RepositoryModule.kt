package com.tajweed.ustoz.di

import com.tajweed.ustoz.data.repository.ProgressRepository
import com.tajweed.ustoz.data.repository.ProgressRepositoryImpl
import com.tajweed.ustoz.data.repository.QuranRepository
import com.tajweed.ustoz.data.repository.QuranRepositoryImpl
import com.tajweed.ustoz.data.repository.RecordingRepository
import com.tajweed.ustoz.data.repository.RecordingRepositoryImpl
import com.tajweed.ustoz.data.repository.TajweedRepository
import com.tajweed.ustoz.data.repository.TajweedRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTajweedRepository(
        tajweedRepositoryImpl: TajweedRepositoryImpl
    ): TajweedRepository

    @Binds
    @Singleton
    abstract fun bindQuranRepository(
        quranRepositoryImpl: QuranRepositoryImpl
    ): QuranRepository

    @Binds
    @Singleton
    abstract fun bindProgressRepository(
        progressRepositoryImpl: ProgressRepositoryImpl
    ): ProgressRepository

    @Binds
    @Singleton
    abstract fun bindRecordingRepository(
        recordingRepositoryImpl: RecordingRepositoryImpl
    ): RecordingRepository
}
