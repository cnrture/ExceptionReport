package com.canerture.exceptionreport.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface.BOLD
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.canerture.exceptionreport.R
import com.canerture.exceptionreport.common.Constants.DEVICE_INFO
import com.canerture.exceptionreport.common.Constants.DURATION
import com.canerture.exceptionreport.common.Constants.EXCEPTION_TEXT
import com.canerture.exceptionreport.common.Constants.THEME_COLOR
import com.canerture.exceptionreport.common.colorRes
import com.canerture.exceptionreport.common.setSafeOnClickListener
import com.canerture.exceptionreport.common.setSpan
import com.canerture.exceptionreport.databinding.ActivityExceptionReportBinding
import com.google.android.material.snackbar.Snackbar

class ExceptionReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExceptionReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExceptionReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        initUI()
    }

    private fun initUI() = with(binding) {

        val exceptionText = intent.getStringExtra(EXCEPTION_TEXT) as String
        val deviceInfo = intent.getStringExtra(DEVICE_INFO) as String
        val themeColor = applicationContext.colorRes(intent.getIntExtra(THEME_COLOR, R.color.black))

        tvTitle.setTextColor(themeColor)
        btnShare.setBackgroundColor(themeColor)
        btnCopy.setBackgroundColor(themeColor)

        tvDeviceInfoText.setDeviceInfo(deviceInfo, themeColor)

        tvExceptionText.text = exceptionText

        btnCopy.setSafeOnClickListener {
            (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).apply {
                setPrimaryClip(
                    ClipData.newPlainText(
                        getString(R.string.text),
                        deviceInfo.plus("\n\n$exceptionText")
                    )
                )
                Snackbar.make(it, getString(R.string.text_copied), DURATION).show()
            }
        }

        btnShare.setSafeOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, deviceInfo.plus("\n\n$exceptionText"))
                type = "text/plain"

                startActivity(Intent.createChooser(this, null))
            }
        }

        btnClose.setSafeOnClickListener {
            finishAffinity()
        }
    }

    private fun TextView.setDeviceInfo(exceptionText: String, color: Int) =
        SpannableString(exceptionText).apply {
            listOf(
                getString(R.string.android_version),
                getString(R.string.device),
                getString(R.string.date)
            ).forEach {
                setSpan(ForegroundColorSpan(color), exceptionText, it)
                setSpan(StyleSpan(BOLD), exceptionText, it)
            }

            text = this
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
}

