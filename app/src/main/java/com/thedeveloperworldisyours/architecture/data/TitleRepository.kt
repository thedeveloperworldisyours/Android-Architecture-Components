package com.thedeveloperworldisyours.architecture.data

import android.app.usage.UsageEvents.Event.NONE
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class TitleRepository(val network: MainNetwork, val titleDao: TitleDao) {

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
    val title: LiveData<String> by lazy<LiveData<String>>(NONE) {
        Transformations.map(titleDao.loadTitle()) { it?.title }
    }

    /**
     * Refresh the current title and save the results to the offline cache.
     *
     * This method does not return the new title. Use [TitleRepository.title] to observe
     * the current tile.
     */
    suspend fun refreshTitle() {
        withContext(Dispatchers.IO) {
            try {
                val result = network.fetchData().await()
                /**
                result.body()?.let {
                    titleDao.insertTitle(Title(it.get(0).title))
                    Log.e("tumadre", it.get(0).title)
                }**/
            } catch (e: HttpException) {
                Log.e("tumadre", e.message)
            } catch (e: Throwable) {
                Log.e("tumadre", e.message)
            }


        }
    }
}


