package com.canerture.exceptionreport.data.model

data class SolutionRequest(
    val messages: List<Message>,
    val model: String = "gpt-3.5-turbo"
)

data class Message(
    val content: String,
    val role: String = "user"
)