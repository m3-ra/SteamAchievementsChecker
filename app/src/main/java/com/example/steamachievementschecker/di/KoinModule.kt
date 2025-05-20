package com.example.steamachievementschecker.di

import com.example.steamachievementschecker.BuildConfig
import com.example.steamachievementschecker.core.AchievementsRepository
import com.example.steamachievementschecker.core.GetMyAchievementsUseCase
import com.example.steamachievementschecker.data.SteamAchievementsRepository
import com.example.steamachievementschecker.data.SteamApi
import com.example.steamachievementschecker.presentation.achievementslist.AchievementsListViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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

val appModule = apiModule + viewModelModule + coreModule
