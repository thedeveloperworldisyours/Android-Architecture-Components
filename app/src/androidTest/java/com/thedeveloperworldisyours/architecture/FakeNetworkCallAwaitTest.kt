package com.thedeveloperworldisyours.architecture

import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FakeNetworkCallAwaitTest {

    /**@Test
    fun whenFakeNetworkCallSuccess_resumeWithResult() {
        val subject = makeSuccessCall("the title")
        runBlocking {
            Truth.assertThat(subject.await()).isEqualTo("the title")
        }
    }

    @Test(expected = FakeNetworkException::class)
    fun whenFakeNetworkCallFailure_throws() {
        val subject = makeFailureCall(FakeNetworkException("the error"))
        runBlocking {
            subject.await()
        }
    }**/
}