package com.canerture.exceptionreport.common

import android.content.Context
import android.os.SystemClock
import android.text.SpannableString
import android.text.Spanned
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat

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