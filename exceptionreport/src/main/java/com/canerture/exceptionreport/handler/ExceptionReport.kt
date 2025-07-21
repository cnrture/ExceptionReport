package com.canerture.exceptionreport.handler

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Process
import com.canerture.exceptionreport.R
import com.canerture.exceptionreport.common.Constants
import com.canerture.exceptionreport.common.getCurrentDate
import com.canerture.exceptionreport.ui.ExceptionReportActivity
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

class ExceptionReport(
    private val activity: Activity,
    private var callback: (String, String) -> Unit = { _, _ -> }
) : Thread.UncaughtExceptionHandler {

    private var targetActivity: Class<*>? = null

    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        val stackTraceString = StringWriter().apply { exception.printStackTrace(PrintWriter(this)) }.toString()
        val deviceInfo = StringBuilder().apply {
            append("${activity.getString(R.string.android_version)} ${Build.VERSION.RELEASE}\n")
            append("${Build.BRAND.uppercase()} - ${Build.DEVICE.uppercase()}\n")
            append(getCurrentDate())
        }.toString()

        callback(deviceInfo, stackTraceString)

        Intent(activity, targetActivity ?: ExceptionReportActivity::class.java).apply {
            putExtra(Constants.DEVICE_INFO, deviceInfo)
            putExtra(Constants.EXCEPTION_TEXT, stackTraceString)
            activity.startActivity(this)
        }

        Process.killProcess(Process.myPid())
        exitProcess(2)
    }

    fun setCustomActivity(targetActivity: Class<*>) {
        this.targetActivity = targetActivity
    }
}