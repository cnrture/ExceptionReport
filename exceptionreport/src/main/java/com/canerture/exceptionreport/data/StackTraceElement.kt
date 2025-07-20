package com.canerture.exceptionreport.data

import java.io.Serializable

internal data class StackTraceElement(
    val className: String,
    val methodName: String,
    val fileName: String?,
    val lineNumber: Int,
    val isHighlighted: Boolean = false,
    val isUserCode: Boolean = false
) : Serializable