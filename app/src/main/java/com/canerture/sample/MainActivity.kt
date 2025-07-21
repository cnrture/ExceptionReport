package com.canerture.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.canerture.exceptionreport.handler.ExceptionReport

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Button(
                    onClick = { throw NullPointerException("Null data!") },
                ) {
                    Text(text = stringResource(R.string.text_exception_button))
                }
            }
        }

        ExceptionReport(this) { deviceInfo, exceptionText ->
            // Handle the exception data here
            // For example, you can log it or send it to a server
            println("Device Info: $deviceInfo")
            println("Exception Text: $exceptionText")
        }
        //.setCustomActivity(CustomExceptionActivity::class.java)
    }
}