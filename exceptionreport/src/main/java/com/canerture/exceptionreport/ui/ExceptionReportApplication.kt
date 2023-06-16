package com.canerture.exceptionreport.ui

import android.app.Application
import com.canerture.exceptionreport.data.repository.SolutionRepositoryImpl
import com.canerture.exceptionreport.data.service.SolutionService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ExceptionReportApplication : Application() {

    companion object {

        private const val TIMEOUT = 60L

        private const val BASE_URL = "https://api.openai.com/v1/chat/"

        var solutionRepositoryImpl: SolutionRepositoryImpl? = null

        fun createSolutionService(apiKey: String) {

            val solutionService: SolutionService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(
                    OkHttpClient.Builder().apply {
                        addInterceptor { chain ->
                            chain.proceed(
                                chain.request().newBuilder()
                                    .header("Authorization", apiKey)
                                    .header("Content-Type", "application/json")
                                    .build()
                            )
                        }
                        readTimeout(TIMEOUT, TimeUnit.SECONDS)
                        connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                        writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    }.build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SolutionService::class.java)

            solutionRepositoryImpl = SolutionRepositoryImpl(solutionService)
        }
    }
}