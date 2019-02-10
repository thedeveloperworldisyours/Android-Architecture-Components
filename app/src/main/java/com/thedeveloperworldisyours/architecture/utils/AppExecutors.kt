package com.thedeveloperworldisyours.architecture.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newFixedThreadPoolContext

const val THREAD_COUNT = 3

open class AppExecutors (
    val ioContext: CoroutineDispatcher = Dispatchers.IO,
    val networkContext: CoroutineDispatcher = newFixedThreadPoolContext(THREAD_COUNT, "networkIO"),
    val uiContext: CoroutineDispatcher = Dispatchers.Main
)