package com.thedeveloperworldisyours.architecture.git.source.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.thedeveloperworldisyours.architecture.git.GithubRepos
import kotlinx.coroutines.Deferred
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface GithubApiService {

    companion object {
        fun create(): GithubApiService {

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val client = RepoOkHttpClient.client
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .baseUrl("https://api.github.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(GithubApiService::class.java)
        }
    }

    @GET
    fun getRepositories(@Url url: String): Deferred<GithubRepos>

}