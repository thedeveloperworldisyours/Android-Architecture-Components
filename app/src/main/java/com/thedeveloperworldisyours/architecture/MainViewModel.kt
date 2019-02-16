package com.thedeveloperworldisyours.architecture

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.thedeveloperworldisyours.architecture.data.TitleRepository
import com.thedeveloperworldisyours.architecture.git.Repo
import com.thedeveloperworldisyours.architecture.git.source.ReposRepository
import com.thedeveloperworldisyours.architecture.git.source.Result
import com.thedeveloperworldisyours.architecture.utils.singleArgViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(private val reposRepository: ReposRepository) : ViewModel() {

    companion object {
        /**
         * Factory for creating [MainViewModel]
         *
         * @param arg the repository to pass to [MainViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::MainViewModel)
    }

    /**
     * Request a snackbar to display a string.
     *
     * This variable is private because we don't want to expose MutableLiveData
     *
     * MutableLiveData allows anyone to set a value, and MainViewModel is the only
     * class that should be setting values.
     */
    private val _snackBar = MutableLiveData<String>()

    /**
     * Request a snackbar to display a string.
     */
    val snackbar: MutableLiveData<String>
        get() = _snackBar

    /**
     * Update title text via this livedata
     */
    val title = reposRepository.title

    private val _spinner = MutableLiveData<Boolean  >()
    /**
     * Show a loading spinner if true
     */
    val spinner: LiveData<Boolean>
        get() = _spinner

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Respond to onClick events by refreshing the title.
     *
     * The loading spinner will display until a result is returned, and errors will trigger
     * a snackbar.
     */
    fun onMainViewClicked() {
        refreshTitle()
    }

    /**
     * Called immediately after the UI shows the snackbar.
     */
    fun onSnackbarShown() {
        _snackBar.value = null
    }

    /**
     * Refresh the title, showing a loading spinner while it refreshes and errors via snackbar.
     */
    fun refreshTitle() {
        /**launchDataLoad {
        repository.refreshTitle()
        }**/
        launchDataLoad {
            val result = reposRepository.getRepos()

            if (result is Result.Success) {
                val reposToShow = ArrayList<Repo>()

                for (repo in result.data) {
                    reposToShow.add(repo)
                }
                _snackBar.value = reposToShow.get(0).name
                /**if (reposView.isActive) {
                    reposView.setLoadingIndicator(false)
                    processRepos(reposToShow)
                }

                //EspressoIdlingResource.decrement()

            } else {
                if (reposView.isActive) {
                    reposView.showLoadingReposError()
                }**/
            }
        }
    }

    /**
     * Helper function to call a data load function with a loading spinner, errors will trigger a
     * snackbar.
     *
     * By marking `block` as `suspend` this creates a suspend lambda which can call suspend
     * functions.
     *
     * @param block lambda to actually load data. It is called in the uiScope. Before calling the
     *              lambda the loading spinner will display, after completion or error the loading
     *              spinner will stop
     */
    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return uiScope.launch {
            try {
                _spinner.value = true
                block()
            } finally {
                _spinner.value = false
            }
        }
    }
}