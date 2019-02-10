package com.thedeveloperworldisyours.architecture.git.source

import com.thedeveloperworldisyours.architecture.git.Repo

interface ReposDataSource {

    suspend fun getRepos(): Result<List<Repo>>

}