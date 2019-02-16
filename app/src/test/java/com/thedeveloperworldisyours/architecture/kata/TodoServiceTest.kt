package com.thedeveloperworldisyours.architecture.kata

import com.thedeveloperworldisyours.architecture.kata.Resource.GET_TASKS_RESPONSE
import com.thedeveloperworldisyours.architecture.kata.Resource.GET_TASK_BY_ID_RESOURCE
import com.thedeveloperworldisyours.architecture.kata.dto.TaskDto
import com.thedeveloperworldisyours.architecture.kata.exception.UnknownApiError
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TodoApiClientTest : MockWebServerTest() {

    companion object {
        private const val ANY_TASK_ID = "1"
        private val ANY_TASK = TaskDto("1", "2", "Finish this kata", false)
    }

    private lateinit var apiClient: TodoApiClient

    @Before
    override fun setUp() {
        super.setUp()
        val mockWebServerEndpoint = baseEndpoint
        apiClient = TodoApiClient(mockWebServerEndpoint)
    }

    @Test
    fun sendsAcceptAndContentTypeHeaders() {
        enqueueMockResponseJSON(200, GET_TASKS_RESPONSE)

        apiClient.allTasks

        assertRequestContainsHeader("Accept", "application/json")
    }

    @Test
    fun sendsContentTypeHeader() {
        enqueueMockResponseJSON(200, GET_TASKS_RESPONSE)

        apiClient.allTasks

        assertRequestContainsHeader("Content-Type", "application/json")
    }

    @Test
    fun sendsGetAllTaskRequestToTheCorrectEndpoint() {
        enqueueMockResponseJSON(200, GET_TASKS_RESPONSE)

        apiClient.allTasks

        assertGetRequestSentTo("/todos")
    }

    @Test
    fun parsesTasksProperlyGettingAllTheTasks(){
        enqueueMockResponseJSON(200, GET_TASKS_RESPONSE)

        val tasks = apiClient.allTasks.right!!

        assertEquals(2, tasks.size.toLong())
    }

    @Test
    fun throwsUnknownErrorExceptionIfThereIsNotHandledErrorGettingAllTasks() {
        enqueueMockResponseJSON(418, "")

        val error = apiClient.allTasks.left!!

        assertEquals(UnknownApiError(418), error)
    }

    @Test
    fun sendsGetTaskByIdRequestToTheCorrectPath() {
        enqueueMockResponseJSON(200, GET_TASK_BY_ID_RESOURCE)

        apiClient.getTaskById(ANY_TASK_ID)

        assertGetRequestSentTo("/todos/" + ANY_TASK_ID)
    }


}