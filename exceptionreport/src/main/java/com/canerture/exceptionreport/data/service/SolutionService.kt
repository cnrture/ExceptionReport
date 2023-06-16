package com.canerture.exceptionreport.data.service

import com.canerture.exceptionreport.data.model.SolutionRequest
import com.canerture.exceptionreport.data.model.SolutionResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface SolutionService {

    @POST("completions")
    suspend fun getSuggestionResponse(@Body solutionRequest: SolutionRequest): SolutionResponse
}