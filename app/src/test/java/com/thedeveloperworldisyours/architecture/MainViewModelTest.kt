package com.thedeveloperworldisyours.architecture

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.capture
import com.nhaarman.mockito_kotlin.inOrder
import com.thedeveloperworldisyours.architecture.data.TitleDao
import com.thedeveloperworldisyours.architecture.data.TitleRepository
import com.thedeveloperworldisyours.architecture.git.Repo
import com.thedeveloperworldisyours.architecture.git.source.DataSourceException
import com.thedeveloperworldisyours.architecture.git.source.ReposDataSource
import com.thedeveloperworldisyours.architecture.git.source.ReposRepository
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import com.thedeveloperworldisyours.architecture.git.source.Result
import org.junit.Assert.assertThat

class MainViewModelTest {

    private val TITLE1 = "retrofit"
    private val DESCRIPTION1 = "Type-safe HTTP client for Android and Java by Square, Inc."
    private val URL1 = "https://github.com/square/retrofit"
    private val TITLE2 = "okhttp"
    private val DESCRIPTION2 = "An HTTP+HTTP/2 client for Android and Java applications."
    private val URL2 = "https://github.com/square/okhttp"
    private val TITLE3 = "glide"
    private val DESCRIPTION3 = "An image loading and caching library for Android focused on smooth scrolling"
    private val URL3 = "https://github.com/bumptech/glide"


    @Mock
    private lateinit var reposRepository: ReposRepository

    private lateinit var mainViewModel: MainViewModel

    private lateinit var repos: List<Repo>

    @Before
    fun setupReposPresenter() {
        MockitoAnnotations.initMocks(this)
        mainViewModel = MainViewModel(reposRepository)


        repos = mutableListOf(
            Repo("1", TITLE1, DESCRIPTION1, URL1),
            Repo("2", TITLE2, DESCRIPTION2, URL2),
            Repo("3", TITLE3, DESCRIPTION3, URL3))
    }
/**
    @Test
    fun loadsTitleByDefault() {
        val subject = MainViewModel(
            ReposRepository(
                Result.Success(repos.get(0).name),
                TitleDao(TITLE1)
            )
        )

        assertThat(subject.title.getValueForTest()).isEqualTo("title")
    }



    @Test
    fun unavailableRepos_ShowsError() = runBlockingSilent {
        setReposNotAvailable(reposRepository)

        with(reposPresenter) {
            loadRepos(true)
        }

        verify(reposView).showLoadingReposError()
    }

    @Test
    fun loadAllReposFromRepositoryAndLoadIntoView() {
        setReposAvailable(reposRepository, repos)

        with(reposPresenter) {
            loadRepos(true)
        }

        verify(reposRepository).getRepos()

        val inOrder = inOrder(reposView)
        inOrder.verify(reposView).setLoadingIndicator(true)
        inOrder.verify(reposView).setLoadingIndicator(false)
        val showReposArgumentCaptor = argumentCaptor<List<Repo>>()
        verify(reposView).showRepos(capture(showReposArgumentCaptor))
        assertTrue(showReposArgumentCaptor.value.size == 3)
    }

    private suspend fun setReposAvailable(dataSource: ReposDataSource, repos: List<Repo>) {
        `when`(dataSource.getRepos()).thenReturn(Result.Success(repos))
    }

    private suspend fun setReposNotAvailable(dataSource: ReposDataSource) {
        `when`(dataSource.getRepos()).thenReturn(Result.Error(DataSourceException()))
    }
*/
}