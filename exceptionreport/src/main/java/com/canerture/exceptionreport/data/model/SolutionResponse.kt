package com.canerture.exceptionreport.data.model

data class SolutionResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: MessageResponse
)

data class MessageResponse(
    val content: String
)
