package com.vk.id.health.metrics.gitlab

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

internal class GitlabApiService(
    private val token: String
) : GitlabApi by Retrofit.Builder()
    .baseUrl("https://gitlab.mvk.com/api/v4/")
    .client(
        OkHttpClient.Builder()
            .addInterceptor { it.proceed(it.request().newBuilder().addHeader("Private-Token", token).build()) }
            .addInterceptor(HttpLoggingInterceptor(::println).apply { level = Level.BODY })
            .build()
    )
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create<GitlabApi>()
