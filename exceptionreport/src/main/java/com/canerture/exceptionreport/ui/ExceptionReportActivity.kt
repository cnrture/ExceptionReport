package com.canerture.exceptionreport.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canerture.exceptionreport.R
import com.canerture.exceptionreport.common.Constants.DEVICE_INFO
import com.canerture.exceptionreport.common.Constants.EXCEPTION_TEXT
import com.canerture.exceptionreport.common.Constants.PARSED_STACK_TRACE
import com.canerture.exceptionreport.data.StackTraceElement

internal class ExceptionReportActivity : ComponentActivity() {

    private val exceptionText by lazy { intent.getStringExtra(EXCEPTION_TEXT) as String }
    private val deviceInfo by lazy { intent.getStringExtra(DEVICE_INFO) as String }
    private val parsedStackTrace by lazy {
        intent.getSerializableExtra(PARSED_STACK_TRACE) as? ArrayList<StackTraceElement> ?: arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(24.dp),
                containerColor = Color.White,
                bottomBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(),
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
                            Text(text = stringResource(R.string.copy))
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Button(
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(),
                            onClick = {
                                Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, deviceInfo.plus("\n\n$exceptionText"))
                                    type = "text/plain"

                                    startActivity(Intent.createChooser(this, null))
                                }
                            },
                        ) {
                            Text(text = stringResource(R.string.share))
                        }
                    }
                },
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.title_exception),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = deviceInfo,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )

                    if (parsedStackTrace.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(parsedStackTrace) { element ->
                                StackTraceItem(element)
                            }
                        }
                    } else {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState()),
                            text = exceptionText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Monospace,
                        )
                    }
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun StackTraceItem(element: StackTraceElement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                element.isHighlighted -> Color(0xFFFFEBEE)
                element.isUserCode -> Color(0xFFF3E5F5)
                else -> Color(0xFFF5F5F5)
            }
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (element.isHighlighted) 4.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "${element.className}.${element.methodName}",
                fontSize = 14.sp,
                fontWeight = if (element.isHighlighted) FontWeight.Bold else FontWeight.Medium,
                fontFamily = FontFamily.Monospace,
                color = if (element.isHighlighted) Color(0xFFD32F2F) else Color.Black
            )

            element.fileName?.let { fileName ->
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "$fileName:${element.lineNumber}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            if (element.isHighlighted) {
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "⚠️ Your Code - Check this line",
                    fontSize = 12.sp,
                    color = Color(0xFFD32F2F),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
