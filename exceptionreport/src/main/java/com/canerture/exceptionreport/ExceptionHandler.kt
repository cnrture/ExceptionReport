package com.canerture.exceptionreport

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Process
import androidx.annotation.ColorRes
import java.io.PrintWriter
import java.io.StringWriter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

class ExceptionHandler : Thread.UncaughtExceptionHandler {

    private var onExceptionReceived: (String) -> Unit = {}

    fun init(
        activity: Activity,
        @ColorRes themeColor: Int? = null
    ) {
        Thread.setDefaultUncaughtExceptionHandler(this)

        onExceptionReceived = { exceptionText ->
            Intent(activity, ExceptionReportActivity::class.java).apply {
                putExtra("exceptionText", exceptionText)
                themeColor?.let { putExtra("themeColor", it) }
                activity.startActivity(this)
            }
            Process.killProcess(Process.myPid())
            exitProcess(2)
        }
    }

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        val stackTrace = StringWriter()
        exception.printStackTrace(PrintWriter(stackTrace))

        StringBuilder().apply {
            append("Android Version: ${Build.VERSION.RELEASE}\n")
            append("Device: ${Build.BRAND.uppercase()} - ${Build.DEVICE.uppercase()}\n")
            append("Date: ${getDate()}\n\n")
            append("Error\n")
            append("$stackTrace\n")
            onExceptionReceived(this.toString())
        }
    }

    private fun getDate(): String {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date).toString()
    }
}
