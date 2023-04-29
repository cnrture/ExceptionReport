package com.canerture.exceptionreport

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Color
import android.graphics.Typeface.BOLD
import android.os.Bundle
import android.text.Layout.Alignment.ALIGN_CENTER
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.AlignmentSpan.Standard
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.canerture.exceptionreport.Const.EXCEPTION_TEXT
import com.canerture.exceptionreport.Const.THEME_COLOR
import com.canerture.exceptionreport.databinding.ActivityExceptionBinding
import com.google.android.material.snackbar.Snackbar

class ExceptionReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExceptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExceptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val exceptionText = intent.getStringExtra(EXCEPTION_TEXT) as String
        val themeColor = intent.getIntExtra(THEME_COLOR, R.color.black)

        val color = ContextCompat.getColor(this@ExceptionReportActivity, themeColor)

        with(binding) {

            window.apply {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                statusBarColor = color
            }

            tvTitle.setTextColor(color)
            btnClose.apply {
                setStrokeColorResource(themeColor)
                setTextColor(color)
            }

            btnCopy.setBackgroundColor(color)

            tvExceptionText.setExceptionText(exceptionText, color)

            btnCopy.setOnClickListener {
                (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).apply {
                    setPrimaryClip(ClipData.newPlainText(getString(R.string.text), exceptionText))
                    Snackbar.make(it, getString(R.string.text_copied), DURATION).show()
                }
            }

            btnClose.setOnClickListener {
                finishAffinity()
            }
        }
    }

    private fun TextView.setExceptionText(exceptionText: String, color: Int) {
        SpannableString(exceptionText).apply {
            listOf(
                getString(R.string.android_version),
                getString(R.string.device),
                getString(R.string.date),
                getString(R.string.error)
            ).forEach {
                setSpan(ForegroundColorSpan(color), exceptionText, it)
                setSpan(StyleSpan(BOLD), exceptionText, it)

                if (it != getString(R.string.error))
                    setSpan(Standard(ALIGN_CENTER), exceptionText, it)
            }

            text = this
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
    }

    private fun SpannableString.setSpan(spanType: Any, fullText: String, colorText: String) =
        setSpan(
            spanType,
            fullText.indexOf(colorText),
            fullText.indexOf(colorText) + colorText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

    companion object {
        private const val DURATION = 1000
    }
}

