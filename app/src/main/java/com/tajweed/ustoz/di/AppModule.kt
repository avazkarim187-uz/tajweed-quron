package com.tajweed.ustoz.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tajweed.ustoz.data.local.DatabaseCallback
import com.tajweed.ustoz.data.local.QuranAyahDao
import com.tajweed.ustoz.data.local.RecordingResultDao
import com.tajweed.ustoz.data.local.TajweedDatabase
import com.tajweed.ustoz.data.local.TajweedRuleDao
import com.tajweed.ustoz.data.local.UserProgressDao
import com.tajweed.ustoz.data.remote.WhisperApiService
import com.tajweed.ustoz.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        callback: DatabaseCallback
    ): TajweedDatabase {
        return Room.databaseBuilder(
            context,
            TajweedDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideTajweedRuleDao(database: TajweedDatabase): TajweedRuleDao {
        return database.tajweedRuleDao()
    }

    @Provides
    fun provideQuranAyahDao(database: TajweedDatabase): QuranAyahDao {
        return database.quranAyahDao()
    }

    @Provides
    fun provideUserProgressDao(database: TajweedDatabase): UserProgressDao {
        return database.userProgressDao()
    }

    @Provides
    fun provideRecordingResultDao(database: TajweedDatabase): RecordingResultDao {
        return database.recordingResultDao()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val apiKeyInterceptor = Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "Bearer ${getApiKey()}")
                .build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.OPENAI_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWhisperApiService(retrofit: Retrofit): WhisperApiService {
        return retrofit.create(WhisperApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    private fun getApiKey(): String {
        // API key should be stored securely, e.g., in BuildConfig or encrypted preferences
        return ""
    }
}
