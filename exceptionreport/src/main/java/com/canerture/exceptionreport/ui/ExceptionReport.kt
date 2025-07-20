package com.canerture.exceptionreport.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Process
import com.canerture.exceptionreport.R
import com.canerture.exceptionreport.common.Constants.DATE_FORMAT
import com.canerture.exceptionreport.common.Constants.DEVICE_INFO
import com.canerture.exceptionreport.common.Constants.EXCEPTION_TEXT
import java.io.PrintWriter
import java.io.StringWriter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.system.exitProcess

class ExceptionReport(private val activity: Activity) : Thread.UncaughtExceptionHandler {

    private var onExceptionReceived: (String, String) -> Unit = { _, _ -> }

    private var targetActivity: Class<*>? = null

    init {
        Thread.setDefaultUncaughtExceptionHandler(this)

        onExceptionReceived = { deviceInfo, exceptionText ->
            Intent(activity, targetActivity ?: ExceptionReportActivity::class.java).apply {
                putExtra(DEVICE_INFO, deviceInfo)
                putExtra(EXCEPTION_TEXT, exceptionText)
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
                append("${Build.BRAND.uppercase()} - ${Build.DEVICE.uppercase()}\n")
                append("${getDate()}")

                onExceptionReceived(this.toString(), stackTrace.toString())
            }
        }
    }

    private fun getDate(): String {
        val dateFormat: DateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val date = Date()
        return dateFormat.format(date).toString()
    }
}
