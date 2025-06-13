package com.jc.steamachievementschecker.di

import androidx.room.Room
import com.jc.steamachievementschecker.BuildConfig
import com.jc.steamachievementschecker.core.AchievementsRepository
import com.jc.steamachievementschecker.core.GetMyAchievementsUseCase
import com.jc.steamachievementschecker.data.db.AppDatabase
import com.jc.steamachievementschecker.data.db.GameInfoDao
import com.jc.steamachievementschecker.data.network.SteamAchievementsRepository
import com.jc.steamachievementschecker.data.network.SteamApi
import com.jc.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val DATABASE_NAME = "app-database"

private val apiModule = module {
    single<Retrofit> {
        Retrofit
            .Builder()
            .client(getOkHttpBuilder())
            .baseUrl("https://api.steampowered.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { getSteamApi(get()) }
}

private fun getSteamApi(retrofit: Retrofit): SteamApi = retrofit.create(SteamApi::class.java)

private fun getOkHttpBuilder(): OkHttpClient =
    OkHttpClient
        .Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(buildHttpLogger())
        .build()

private fun buildHttpLogger(): Interceptor =
    HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
    }

private val viewModelModule = module {
    viewModelOf(::AchievementsListViewModel)
}

private val coreModule = module {
    singleOf(::SteamAchievementsRepository).bind(AchievementsRepository::class)
    singleOf(::GetMyAchievementsUseCase)
}

private val daoModule = module {
    single<AppDatabase> {
        Room
            .databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration(true)
            .build()
    }

    single<GameInfoDao> { get<AppDatabase>().gameInfoDao() }
}

val appModule = apiModule + viewModelModule + coreModule + daoModule
