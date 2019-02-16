package com.thedeveloperworldisyours.architecture.kata

object Resource {
    val GET_TASKS_RESPONSE: String =
        "[{\"userId\": 1,\"id\": 1,\"title\": \"delectus aut autem\",\"completed\": false },{ \"userId\": 10,\"id\": 200,\"title\": \"ipsam aperiam voluptates qui\",\"completed\": false }]"
    val GET_TASK_BY_ID_RESOURCE: String = "{\n" +
            "  \"userId\": 1,\n" +
            "  \"id\": 1,\n" +
            "  \"title\": \"delectus aut autem\",\n" +
            "  \"completed\": false\n" +
            "}"
}