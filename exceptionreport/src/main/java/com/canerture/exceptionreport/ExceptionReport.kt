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

class ExceptionReport(
    private val activity: Activity,
    @ColorRes private val themeColor: Int? = null
) : Thread.UncaughtExceptionHandler {

    private var onExceptionReceived: (String) -> Unit = {}

    init {
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

        with(activity) {
            StringBuilder().apply {
                append("${getString(R.string.android_version)} ${Build.VERSION.RELEASE}\n")
                append("${getString(R.string.device)} ${Build.BRAND.uppercase()} - ${Build.DEVICE.uppercase()}\n")
                append("${getString(R.string.date)} ${getDate()}\n\n")
                append("${getString(R.string.error)}\n")
                append("$stackTrace\n")

                onExceptionReceived(this.toString())
            }
        }
    }

    private fun getDate(): String {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date).toString()
    }
}
