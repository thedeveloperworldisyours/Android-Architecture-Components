package com.thedeveloperworldisyours.architecture.git.source.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RepoOkHttpClient {

    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

}