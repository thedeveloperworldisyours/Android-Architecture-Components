package com.thedeveloperworldisyours.architecture

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.thedeveloperworldisyours.architecture.data.MainNetworkImpl
import com.thedeveloperworldisyours.architecture.data.RetrofitService
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitServiceTest {
    lateinit var service: RetrofitService

    @Before
    internal fun setUp() {

        service = Retrofit.Builder()
            .baseUrl(MainNetworkImpl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(RetrofitService::class.java)
    }

    @Test
    internal fun should_callServiceWithCoroutine() {
        runBlocking {
            val repos = service.getPosts().await()
            repos.isSuccessful
        }
    }
}