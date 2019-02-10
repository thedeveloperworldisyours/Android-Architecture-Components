package com.thedeveloperworldisyours.architecture.git.source

import android.app.usage.UsageEvents
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.util.Log
import com.thedeveloperworldisyours.architecture.data.Title
import com.thedeveloperworldisyours.architecture.data.TitleDao
import com.thedeveloperworldisyours.architecture.data.TitleRepository
import com.thedeveloperworldisyours.architecture.git.Repo

class ReposRepository(
    val reposRemoteDataSource: ReposDataSource, val titleDao: TitleDao
) : ReposDataSource {
    /**
     * [LiveData] to load title.
     *
     * This is the main interface for loading a title. The title will be loaded from the offline
     * cache.
     *
     * Observing this will not cause the title to be refreshed, use [TitleRepository.refreshTitle]
     * to refresh the title.
     *
     * Because this is defined as `by lazy` it won't be instantiated until the property is
     * used for the first time.
     */
    val title: LiveData<String> by lazy<LiveData<String>>(UsageEvents.Event.NONE) {
        Transformations.map(titleDao.loadTitle()) { it?.title }
    }



    // This sample project always refreshes by pulling from remote data source everytime
    override suspend fun getRepos(): Result<List<Repo>> {
        return getReposFromRemoteDataSource()
    }

    private suspend fun getReposFromRemoteDataSource(): Result<List<Repo>> {
        val result = reposRemoteDataSource.getRepos()
        return when (result) {
            is Result.Success -> {
                result.data.get(0).description?.let {
                    Log.e("tu-->", result.data.get(0).name)
                    //titleDao.insertTitle(Title(result.data.get(0).name!!))
                    //Title(result.data.get(0).description!!)
                }
                Result.Success(result.data)
            }
            is Result.Error -> {
                Result.Error(RemoteDataNotFoundException())
            }
        }
    }

    companion object {
        private var INSTANCE: ReposRepository? = null

        @JvmStatic
        fun getInstance(reposRemoteDataSource: ReposDataSource, titleDao: TitleDao): ReposRepository {
            return INSTANCE
                ?: ReposRepository(reposRemoteDataSource, titleDao).apply { INSTANCE = this }
        }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}