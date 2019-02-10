package com.thedeveloperworldisyours.architecture

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.thedeveloperworldisyours.architecture.data.MainNetworkImpl
import com.thedeveloperworldisyours.architecture.data.TitleRepository
import com.thedeveloperworldisyours.architecture.data.getDatabase
import com.thedeveloperworldisyours.architecture.git.source.ReposDataSource
import com.thedeveloperworldisyours.architecture.git.source.ReposRepository
import com.thedeveloperworldisyours.architecture.git.source.remote.ReposRemoteDataSource
import com.thedeveloperworldisyours.architecture.utils.AppExecutors
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        // Get MainViewModel by passing a database to the factory
        val database = getDatabase(this)
        val repository =
            ReposRepository.getInstance(
                ReposRemoteDataSource.newInstance(AppExecutors()), database.titleDao)
        val viewModel = ViewModelProviders
            .of(this, MainViewModel.FACTORY(repository))
            .get(MainViewModel::class.java)

        // When rootLayout is clicked call onMainViewClicked in ViewModel
        rootLayout.setOnClickListener {
            viewModel.onMainViewClicked()
        }

        // update the title when the [MainViewModel.title] changes
        viewModel.title.observe(this, Observer { value ->
            value?.let {
                title_textView.text = it
            }
        })

        // show the spinner when [MainViewModel.spinner] is true
        viewModel.spinner.observe(this, Observer { value ->
            value?.let { show ->
                spinner.visibility = if (show) View.VISIBLE else View.GONE
            }
        })

        // Show a snackbar whenever the [ViewModel.snackbar] is updated a non-null value
        viewModel.snackbar.observe(this, Observer { text ->
            text?.let {
                Snackbar.make(rootLayout, text, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackbarShown()
            }
        })
    }

}
