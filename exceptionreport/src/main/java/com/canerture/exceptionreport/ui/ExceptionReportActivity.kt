package com.canerture.exceptionreport.ui

import android.animation.ValueAnimator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.res.ColorStateList
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
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.canerture.exceptionreport.R
import com.canerture.exceptionreport.common.Constants.API_KEY
import com.canerture.exceptionreport.common.Constants.DEVICE_INFO
import com.canerture.exceptionreport.common.Constants.DURATION
import com.canerture.exceptionreport.common.Constants.EXCEPTION_TEXT
import com.canerture.exceptionreport.common.Constants.THEME_COLOR
import com.canerture.exceptionreport.common.colorRes
import com.canerture.exceptionreport.common.gone
import com.canerture.exceptionreport.common.setSafeOnClickListener
import com.canerture.exceptionreport.common.setSpan
import com.canerture.exceptionreport.common.setViewsGone
import com.canerture.exceptionreport.common.setViewsVisible
import com.canerture.exceptionreport.common.visible
import com.canerture.exceptionreport.databinding.ActivityExceptionReportBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ExceptionReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExceptionReportBinding

    private var viewModel: SolutionViewModel? = null

    private var isSearchButtonVisible = true

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExceptionReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        intent.getStringExtra(API_KEY)?.let { apiKey ->
            ExceptionReportApplication.createSolutionService(apiKey)
            ExceptionReportApplication.solutionRepositoryImpl?.let {
                viewModel = ViewModelProvider(
                    this,
                    SolutionViewModelFactory(it)
                )[SolutionViewModel::class.java]
            }
        }

        initUI()
        collectState()
        collectEffect()
    }

    private fun initUI() = with(binding) {

        val exceptionText = intent.getStringExtra(EXCEPTION_TEXT) as String
        val deviceInfo = intent.getStringExtra(DEVICE_INFO) as String
        val themeColor = applicationContext.colorRes(intent.getIntExtra(THEME_COLOR, R.color.black))
        val whiteColor = applicationContext.colorRes(R.color.white)
        val lightGrayColor = applicationContext.colorRes(R.color.light_gray)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            statusBarColor = themeColor
        }

        selectTab(true, themeColor, whiteColor, lightGrayColor)

        tvSolution.isVisible = viewModel != null

        tvTitle.setTextColor(themeColor)
        fabClose.backgroundTintList = ColorStateList.valueOf(themeColor)
        fabShare.backgroundTintList = ColorStateList.valueOf(themeColor)
        fabCopy.backgroundTintList = ColorStateList.valueOf(themeColor)
        btnSearchForSolution.setBackgroundColor(themeColor)
        progressBar.imageTintList = ColorStateList.valueOf(themeColor)

        tvDeviceInfoText.setDeviceInfo(deviceInfo, themeColor)

        tvExceptionText.text = exceptionText

        tvException.setSafeOnClickListener {
            setViewsGone(btnSearchForSolution, tvSolutionText)
            setViewsVisible(tvExceptionText, fabClose, fabShare, fabCopy)
            selectTab(true, themeColor, whiteColor, lightGrayColor)
        }

        tvSolution.setSafeOnClickListener {
            setViewsGone(tvExceptionText, fabClose, fabShare, fabCopy)
            tvSolutionText.visible()
            btnSearchForSolution.isVisible = isSearchButtonVisible
            selectTab(false, themeColor, whiteColor, lightGrayColor)
        }

        btnSearchForSolution.setSafeOnClickListener {
            viewModel?.getSuggestionResponse(
                getString(R.string.chat_gpt_prompt).plus(exceptionText)
            )
            btnSearchForSolution.gone()
            tvException.isEnabled = false
        }

        fabCopy.setSafeOnClickListener {
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

        fabShare.setSafeOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, deviceInfo.plus("\n\n$exceptionText"))
                type = "text/plain"

                startActivity(Intent.createChooser(this, null))
            }
        }

        fabClose.setSafeOnClickListener {
            finishAffinity()
        }
    }

    private fun collectState() = with(binding) {
        job = CoroutineScope(Dispatchers.IO).launch {
            viewModel?.state?.collect { state ->
                runOnUiThread {
                    setLoading(state.isLoading)
                    state.solution?.let {
                        tvSolutionText.visible()
                        tvSolutionText.text = state.solution
                        tvException.isEnabled = true
                        isSearchButtonVisible = false
                    }
                }
            }
        }
    }

    private fun collectEffect() = with(binding) {
        job = CoroutineScope(Dispatchers.IO).launch {
            viewModel?.effect?.collect { effect ->
                when (effect) {
                    is SolutionEffect.ShowError -> {
                        runOnUiThread {
                            errorText.visible()
                            errorText.text = effect.message
                        }
                    }
                }
            }
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

    private fun setLoading(isLoading: Boolean) =
        binding.progressBar.apply {
            isVisible = isLoading
            ValueAnimator.ofFloat(1f, 1.5f).apply {
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                duration = 500L

                addUpdateListener { animator ->
                    scaleX = animator.animatedValue as Float
                    scaleY = animator.animatedValue as Float
                }

                if (isLoading) start() else cancel()
            }
        }

    private fun selectTab(exceptionShow: Boolean, themeColor: Int, white: Int, lightGray: Int) =
        with(binding) {
            tvException.setBackgroundColor(if (exceptionShow) themeColor else lightGray)
            tvException.setTextColor(if (exceptionShow) white else themeColor)
            tvSolution.setBackgroundColor(if (exceptionShow) lightGray else themeColor)
            tvSolution.setTextColor(if (exceptionShow) themeColor else white)
        }

    override fun onDestroy() {
        super.onDestroy()
        job = null
    }
}

