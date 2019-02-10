package com.thedeveloperworldisyours.architecture.data

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface RetrofitService {
    @GET("/posts")
    fun getPosts(): Deferred<Result<List<Post>>>
}