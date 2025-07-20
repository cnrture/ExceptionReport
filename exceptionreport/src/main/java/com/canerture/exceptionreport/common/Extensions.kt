package com.canerture.exceptionreport.common

import com.canerture.exceptionreport.common.Constants.DATE_FORMAT
import com.canerture.exceptionreport.data.StackTraceElement
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun String.parseStackTrace(packageName: String): List<StackTraceElement> {
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

internal fun String.getPackageName(): String {
    return this.lines()
        .firstOrNull { it.trim().startsWith("at ") && !it.contains("android.") && !it.contains("java.") }
        ?.let { line ->
            val regex = Regex("at ([a-zA-Z][a-zA-Z0-9_]*(?:\\.[a-zA-Z][a-zA-Z0-9_]*)*)")
            regex.find(line.trim())?.groupValues?.get(1)?.substringBeforeLast('.')?.substringBeforeLast('.')
        } ?: ""
}

internal fun getCurrentDate(): String {
    val dateFormat: DateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    val date = Date()
    return dateFormat.format(date).toString()
}