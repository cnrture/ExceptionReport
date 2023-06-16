package com.canerture.exceptionreport.data.repository

import com.canerture.exceptionreport.common.Resource
import com.canerture.exceptionreport.data.model.Message
import com.canerture.exceptionreport.data.model.SolutionRequest
import com.canerture.exceptionreport.data.service.SolutionService
import com.canerture.exceptionreport.domain.repository.SolutionRepository

class SolutionRepositoryImpl(private val solutionService: SolutionService) : SolutionRepository {

    override suspend fun getSuggestionResponse(content: String): Resource<String> = try {
        Resource.Success(
            solutionService.getSuggestionResponse(
                SolutionRequest(listOf(Message(content)))
            ).choices.first().message.content.plus("\n")
        )
    } catch (e: Exception) {
        println(e.toString())
        Resource.Error(e.message.orEmpty())
    }
}