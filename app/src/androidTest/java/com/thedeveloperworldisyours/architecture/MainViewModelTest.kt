package com.thedeveloperworldisyours.architecture

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.thedeveloperworldisyours.architecture.data.MainNetwork
import com.thedeveloperworldisyours.architecture.data.Post
import com.thedeveloperworldisyours.architecture.data.TitleRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MainViewModelTest {

    val expectedRepos = listOf(
        Post(1, 1, "dummy-repo-1", "dummy-repo-1"),
        Post(2, 2, "dummy-repo-2", "dummy-repo-2")
    )
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun loadsTitleByDefault() {
        val subject = MainViewModel(
            TitleRepository(
                MainNetworkFake(Deferred<Result.success(expectedRepos.get(0))),
                TitleDaoFake("title")
            )
        )

        assertThat(subject.title.getValueForTest()).isEqualTo("title")
    }

    @Test
    fun whenSuccessfulTitleLoad_itShowsAndHidesSpinner() {
        val call = FakeNetworkCall<String>()

        val subject = MainViewModel(
            TitleRepository(
                MainNetworkFake(call),
                TitleDaoFake("title")
            )
        )

        subject.spinner.captureValues {
            subject.onMainViewClicked()
            runBlocking {
                assertSendsValues(2_000, true)
                call.onSuccess("data")
                assertSendsValues(2_000, true, false)
            }
        }
    }

    @Test
    fun whenErrorTitleReload_itShowsErrorAndHidesSpinner() {
        val call = FakeNetworkCall<String>()
        val subject = MainViewModel(
            TitleRepository(
                MainNetworkFake(call),
                TitleDaoFake("title")
            )
        )

        subject.spinner.captureValues {
            val spinnerCaptor = this
            subject.onMainViewClicked()
            runBlocking {
                assertSendsValues(2_000, true)
                call.onError(FakeNetworkException("An error"))
                assertSendsValues(2_000, true, false)
            }
        }
    }

    @Test
    fun whenErrorTitleReload_itShowsErrorText() {
        val call = FakeNetworkCall<String>()
        val subject = MainViewModel(
            TitleRepository(
                MainNetworkFake(call),
                TitleDaoFake("title")
            )
        )

        subject.snackbar.captureValues {
            val spinnerCaptor = this
            subject.onMainViewClicked()
            runBlocking {
                call.onError(FakeNetworkException("An error"))
                assertSendsValues(2_000, "An error")
                subject.onSnackbarShown()
                assertSendsValues(2_000, "An error", null)
            }
        }
    }

    @Test
    fun whenMainViewClicked_titleIsRefreshed() {
        val titleDao = TitleDaoFake("title")
        val subject = MainViewModel(
            TitleRepository(
                MainNetworkFake(makeSuccessCall("OK")),
                titleDao
            )
        )
        subject.onMainViewClicked()
        titleDao.assertNextInsert("OK")
    }
}