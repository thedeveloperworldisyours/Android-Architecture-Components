package com.thedeveloperworldisyours.architecture

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.thedeveloperworldisyours.architecture.data.MainNetworkImpl
import com.thedeveloperworldisyours.architecture.data.RetrofitService
import com.thedeveloperworldisyours.architecture.git.source.remote.GithubApiService
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GithubApiServiceTest {
    private var TRENDING_REPOS_URL = "/search/repositories" +
            "?q=android+language:java+language:kotlin&sort=stars&order=desc"

    lateinit var service: GithubApiService

    @Before
    internal fun setUp() {

        service = Retrofit.Builder()
            .baseUrl(MainNetworkImpl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(GithubApiService::class.java)
    }

    @Test
    internal fun should_callServiceWithCoroutine() {
        runBlocking {
            val repos = service.getRepositories(TRENDING_REPOS_URL).await()
            repos
        }
    }
}