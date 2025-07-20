package com.canerture.exceptionreport.common

import android.content.Context
import android.os.SystemClock
import android.text.SpannableString
import android.text.Spanned
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import com.canerture.exceptionreport.common.Constants.DATE_FORMAT
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.colorRes(@ColorRes color: Int) = ResourcesCompat.getColor(resources, color, null)

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun setViewsVisible(vararg views: View) {
    views.forEach { it.visible() }
}

fun setViewsGone(vararg views: View) {
    views.forEach { it.gone() }
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    var lastTimeClicked: Long = 0
    setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < 1000) {
            return@setOnClickListener
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeClick(it)
    }
}

fun SpannableString.setSpan(spanType: Any, fullText: String, colorText: String) =
    setSpan(
        spanType,
        fullText.indexOf(colorText),
        fullText.indexOf(colorText) + colorText.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

data class StackTraceElement(
    val className: String,
    val methodName: String,
    val fileName: String?,
    val lineNumber: Int,
    val isHighlighted: Boolean = false,
    val isUserCode: Boolean = false
) : Serializable

fun String.parseStackTrace(packageName: String): List<StackTraceElement> {
    return this.lines()
        .filter { it.trim().startsWith("at ") }
        .mapNotNull { line ->
            val regex = Regex("at (.+)\\.(.+)\\((.+?):(\\d+)\\)")
            val matchResult = regex.find(line.trim())

            matchResult?.let { match ->
                val fullClassName = match.groupValues[1]
                val methodName = match.groupValues[2]
                val fileName = match.groupValues[3].takeIf { it != "Unknown Source" }
                val lineNumber = match.groupValues[4].toIntOrNull() ?: 0

                val isUserCode = fullClassName.startsWith(packageName)
                val isHighlighted = isUserCode && lineNumber > 0

                StackTraceElement(
                    className = fullClassName.substringAfterLast('.'),
                    methodName = methodName,
                    fileName = fileName,
                    lineNumber = lineNumber,
                    isHighlighted = isHighlighted,
                    isUserCode = isUserCode
                )
            }
        }
}

fun String.getPackageName(): String {
    return this.lines()
        .firstOrNull { it.trim().startsWith("at ") && !it.contains("android.") && !it.contains("java.") }
        ?.let { line ->
            val regex = Regex("at ([a-zA-Z][a-zA-Z0-9_]*(?:\\.[a-zA-Z][a-zA-Z0-9_]*)*)")
            regex.find(line.trim())?.groupValues?.get(1)?.substringBeforeLast('.')?.substringBeforeLast('.')
        } ?: ""
}

fun getCurrentDate(): String {
    val dateFormat: DateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    val date = Date()
    return dateFormat.format(date).toString()
}