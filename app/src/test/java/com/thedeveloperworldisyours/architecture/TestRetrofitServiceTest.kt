package com.thedeveloperworldisyours.architecture

import com.nhaarman.mockito_kotlin.whenever
import com.thedeveloperworldisyours.architecture.data.Post
import com.thedeveloperworldisyours.architecture.data.RetrofitService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response


class TestRetrofitServiceTest {
    val expectedRepos = listOf(
        Post(1, 1, "dummy-repo-1", "dummy-repo-1"),
        Post(2, 2, "dummy-repo-2", "dummy-repo-2")
    )

    @Mock
    lateinit var service: RetrofitService

    @Before
    internal fun setUp() {
        MockitoAnnotations.initMocks(this)

        val deferred = CompletableDeferred<Response<List<Post>>>()
        whenever(service.getPosts()).thenReturn(deferred)
    }

    @Test
    internal fun should_doSomethingWithRemoteDataFetchedWithCoroutines() {
        val actualRepos = runBlocking { service.getPosts().await() }

        actualRepos shouldEqual expectedRepos
    }
}