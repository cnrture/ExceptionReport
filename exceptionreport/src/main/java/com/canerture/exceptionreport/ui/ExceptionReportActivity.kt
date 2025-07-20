package com.canerture.exceptionreport.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canerture.exceptionreport.R
import com.canerture.exceptionreport.common.Constants.DEVICE_INFO
import com.canerture.exceptionreport.common.Constants.EXCEPTION_TEXT
import com.canerture.exceptionreport.common.Constants.THEME_COLOR
import com.canerture.exceptionreport.common.colorRes

class ExceptionReportActivity : AppCompatActivity() {

    private val exceptionText by lazy { intent.getStringExtra(EXCEPTION_TEXT) as String }
    private val deviceInfo by lazy { intent.getStringExtra(DEVICE_INFO) as String }
    private val themeColor by lazy { applicationContext.colorRes(intent.getIntExtra(THEME_COLOR, R.color.black)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).apply {
                                    setPrimaryClip(
                                        ClipData.newPlainText(
                                            getString(R.string.text),
                                            deviceInfo.plus("\n\n$exceptionText")
                                        )
                                    )
                                }
                            },
                        ) {
                            Text(text = stringResource(R.string.share))
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, deviceInfo.plus("\n\n$exceptionText"))
                                    type = "text/plain"

                                    startActivity(Intent.createChooser(this, null))
                                }
                            },
                        ) {
                            Text(text = stringResource(R.string.copy))
                        }
                    }
                },
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                ) {
                    Text(
                        text = stringResource(R.string.title_exception),
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(
                        text = exceptionText,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        text = exceptionText,
                    )
                }
            }
        }
    }
}

