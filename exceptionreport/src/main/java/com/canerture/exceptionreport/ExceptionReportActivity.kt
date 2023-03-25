package com.canerture.exceptionreport

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.AlignmentSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.canerture.exceptionreport.databinding.ActivityExceptionBinding
import com.google.android.material.snackbar.Snackbar

class ExceptionReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExceptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExceptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val exceptionText = intent.getStringExtra("exceptionText") as String
        val themeColor = intent.getIntExtra("themeColor", 0)

        with(binding) {

            val color = ContextCompat.getColor(this@ExceptionReportActivity, themeColor)

            if (themeColor != 0) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.statusBarColor = color

                tvTitle.setTextColor(color)
                btnClose.setStrokeColorResource(themeColor)
                btnClose.setTextColor(color)
                btnCopy.setBackgroundColor(color)
            }

            tvExceptionText.setClarificationText(exceptionText, color)

            btnCopy.setOnClickListener {
                val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("text", tvExceptionText.text.toString())
                clipboardManager.setPrimaryClip(clipData)
                Snackbar.make(it, getString(R.string.text_copied), DURATION).show()
            }

            btnClose.setOnClickListener {
                finishAffinity()
            }
        }
    }

    private fun TextView.setClarificationText(
        fullText: String,
        color: Int
    ) {
        SpannableString(fullText).apply {
            setCustomSpan(
                fullText = fullText,
                color = color,
                Pair("Android Version:", true),
                Pair("Device:", true),
                Pair("Date:", true),
                Pair("Error", false)
            )
            text = this
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
    }

    private fun SpannableString.setCustomSpan(
        fullText: String,
        color: Int,
        vararg text: Pair<String, Boolean>
    ) {
        text.forEach {
            if (it.second)
                setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    fullText.indexOf(it.first),
                    fullText.indexOf(it.first) + it.first.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            setSpan(
                ForegroundColorSpan(color),
                fullText.indexOf(it.first),
                fullText.indexOf(it.first) + it.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                StyleSpan(Typeface.BOLD),
                fullText.indexOf(it.first),
                fullText.indexOf(it.first) + it.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    companion object {
        private const val DURATION = 1000
    }
}