package com.thedeveloperworldisyours.architecture.git.source.remote

import android.util.Log
import com.thedeveloperworldisyours.architecture.git.source.Result
import com.thedeveloperworldisyours.architecture.git.Repo
import com.thedeveloperworldisyours.architecture.git.source.RemoteDataNotFoundException
import com.thedeveloperworldisyours.architecture.git.source.ReposDataSource
import com.thedeveloperworldisyours.architecture.utils.AppExecutors
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ReposRemoteDataSource private constructor(
    val appExecutors: AppExecutors,
    val githubApiService: GithubApiService
) : ReposDataSource {

    private var TRENDING_REPOS_URL = "/search/repositories" +
            "?q=android+language:java+language:kotlin&sort=stars&order=desc"

    override suspend fun getRepos(): Result<List<Repo>> = withContext(appExecutors.networkContext) {
        val request = githubApiService.getRepositories(TRENDING_REPOS_URL)
        try {
            val response = request.await()
            if (response.items.isNotEmpty()) {
                for (repo in response.items)
                    Log.d("githubrepos", repo.name + ": " + repo.html_url)
                Result.Success(response.items)
            } else {
                Result.Error(RemoteDataNotFoundException())
            }
        }
        catch (ex: HttpException) {
            Result.Error(RemoteDataNotFoundException())
        }
        catch (ex: Throwable) {
            Result.Error(RemoteDataNotFoundException())
        }

    }

    companion object {
        fun newInstance(appExecutors: AppExecutors) =
            ReposRemoteDataSource(
                appExecutors,
                GithubApiService.create()
            )
    }
}