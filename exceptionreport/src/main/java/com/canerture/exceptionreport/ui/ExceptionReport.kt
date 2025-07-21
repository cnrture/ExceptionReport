package com.canerture.exceptionreport.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Process
import com.canerture.exceptionreport.R
import com.canerture.exceptionreport.common.Constants.DEVICE_INFO
import com.canerture.exceptionreport.common.Constants.EXCEPTION_TEXT
import com.canerture.exceptionreport.common.getCurrentDate
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

class ExceptionReport(
    private val activity: Activity,
    private var onExceptionReceived: (String, String) -> Unit = { _, _ -> }
) : Thread.UncaughtExceptionHandler {

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
        val stackTraceString = StringWriter().apply { exception.printStackTrace(PrintWriter(this)) }.toString()
        StringBuilder().apply {
            append("${activity.getString(R.string.android_version)} ${Build.VERSION.RELEASE}\n")
            append("${Build.BRAND.uppercase()} - ${Build.DEVICE.uppercase()}\n")
            append(getCurrentDate())

            onExceptionReceived(this.toString(), stackTraceString)
        }
    }

    fun setCustomActivity(targetActivity: Class<*>) {
        this.targetActivity = targetActivity
    }
}
