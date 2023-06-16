package com.canerture.exceptionreport.domain.repository

import com.canerture.exceptionreport.common.Resource

interface SolutionRepository {
    suspend fun getSuggestionResponse(content: String): Resource<String>
}