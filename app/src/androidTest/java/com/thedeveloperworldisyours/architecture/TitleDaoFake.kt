package com.thedeveloperworldisyours.architecture

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.thedeveloperworldisyours.architecture.data.Title
import com.thedeveloperworldisyours.architecture.data.TitleDao
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.google.common.truth.Truth
import com.thedeveloperworldisyours.architecture.data.MainNetwork
import com.thedeveloperworldisyours.architecture.data.Post
import kotlinx.coroutines.*
import retrofit2.Response

class TitleDaoFake(var titleToReturn: String) : TitleDao {
    val inserted = mutableListOf<String>()

    /**
     * This is used to signal an element has been inserted.
     */
    private var nextInsertion: CompletableDeferred<String>? = null

    /**
     * Protect concurrent access to inserted and nextInsertion
     */
    private val mutex = Mutex()

    override fun insertTitle(title: Title) {
        runBlocking {
            mutex.withLock {
                inserted += title.title
                // complete the waiting deferred
                nextInsertion?.complete(title.title)
            }
        }
    }

    override fun loadTitle(): LiveData<Title> {
        return MutableLiveData<Title>().apply {
            value = Title(titleToReturn)
        }
    }

    /**
     * Assertion that the next element inserted has a title of expected
     *
     * If the element was previously inserted and is currently the most recent element
     * this assertion will also match. This allows tests to avoid synchronizing calls to insert
     * with calls to assertNextInsert.
     *
     * @param expected the value to match
     * @param timeout duration to wait
     * @param unit timeunit
     */
    fun assertNextInsert(expected: String, timeout: Long = 2_000) {
        runBlocking {
            val completableDeferred = CompletableDeferred<String>()
            mutex.withLock {
                // first check if the last element is already expected
                if (inserted.isNotEmpty() && inserted.last() == expected) {
                    return@runBlocking
                }

                // if not, setup a deferred to get notified of next insertion
                nextInsertion = completableDeferred
            }

            // wait for the next insertion to complete the deferred
            try {
                withTimeout(timeout) {
                    val next = completableDeferred.await()
                    Truth.assertThat(next).isEqualTo(expected)
                }
            } catch (ex: TimeoutCancellationException) {
                // generate a nice stack trace
                Truth.assertThat(ex).isEqualTo(expected)
            }

        }
    }
}

/**
 * Testing Fake implementation of MainNetwork
 */
/**
 * Testing Fake implementation of MainNetwork
 */
class MainNetworkFake(var call: Deferred<Response<List<Post>>> = makeSuccessCall("title")) : MainNetwork {
    override fun fetchData(): Deferred<Response<List<Post>>> {
        return call
    }
}

/**
 * Make a fake successful network result
 *
 * @param result result to return
 */
fun <T> makeSuccessCall(result: T) = Response<T>().apply {
    onSuccess(result)
}

/**
 * Make a fake failed network call
 *
 * @param throwable error to wrap
 */
fun makeFailureCall(throwable: FakeNetworkException) = FakeNetworkCall<String>().apply {
    onError(throwable)
}