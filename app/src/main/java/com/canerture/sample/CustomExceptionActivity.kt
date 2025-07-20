package com.canerture.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

class CustomExceptionActivity : AppCompatActivity() {

    private val exceptionText = intent.getStringExtra("exceptionText") as String
    private val deviceInfo = intent.getStringExtra("deviceInfo") as String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Text(
                    text = stringResource(R.string.title_exception),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = deviceInfo,
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    text = exceptionText,
                )
            }
        }
    }
}

