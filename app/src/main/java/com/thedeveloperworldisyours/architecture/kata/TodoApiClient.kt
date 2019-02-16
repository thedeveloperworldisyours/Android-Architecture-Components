package com.thedeveloperworldisyours.architecture.kata

import com.thedeveloperworldisyours.architecture.kata.TodoApiClientConfig.BASE_ENDPOINT
import com.thedeveloperworldisyours.architecture.kata.dto.TaskDto
import com.thedeveloperworldisyours.architecture.kata.exception.ItemNotFoundError
import com.thedeveloperworldisyours.architecture.kata.exception.NetworkError
import com.thedeveloperworldisyours.architecture.kata.exception.TodoApiClientError
import com.thedeveloperworldisyours.architecture.kata.exception.UnknownApiError
import com.thedeveloperworldisyours.architecture.kata.interceptor.DefaultHeadersInterceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class TodoApiClient @JvmOverloads constructor(baseEndpoint: String = BASE_ENDPOINT) {

    private val todoService: TodoService

    init {
        val client = OkHttpClient().newBuilder().addInterceptor(DefaultHeadersInterceptor()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseEndpoint)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        this.todoService = retrofit.create(TodoService::class.java)
    }

    val allTasks: Either<TodoApiClientError, List<TaskDto>>
        get() = try {
            val response = todoService.all.execute()
            inspectResponseForErrors(response)
        } catch (e: IOException) {
            Either.left(NetworkError)
        }

    fun getTaskById(taskId: String): Either<TodoApiClientError, TaskDto> = try {
        val response = todoService.getById(taskId).execute()
        inspectResponseForErrors(response)
    } catch (e: IOException) {
        Either.left(NetworkError)
    }

    fun addTask(task: TaskDto): Either<TodoApiClientError, TaskDto> = try {
        val response = todoService.add(task).execute()
        inspectResponseForErrors(response)
    } catch (e: IOException) {
        Either.left(NetworkError)
    }

    fun updateTaskById(task: TaskDto): Either<TodoApiClientError, TaskDto> = try {
        val response = todoService.updateById(task.id, task).execute()
        inspectResponseForErrors(response)
    } catch (e: IOException) {
        Either.left(NetworkError)
    }

    fun deleteTaskById(taskId: String): TodoApiClientError? = try {
        val response = todoService.deleteById(taskId).execute()
        inspectResponseForErrors(response).left
    } catch (e: IOException) {
        NetworkError
    }

    private fun <T> inspectResponseForErrors(response: Response<T>): Either<TodoApiClientError, T> = when {
        response.code() == 404 -> Either.left(ItemNotFoundError)
        response.code() >= 400 -> Either.left(UnknownApiError(response.code()))
        response.body() == null -> Either.left(UnknownApiError(response.code()))
        else -> Either.right(response.body()!!)
    }
}