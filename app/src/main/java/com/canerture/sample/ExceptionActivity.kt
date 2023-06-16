package com.canerture.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.canerture.sample.databinding.ActivityExceptionBinding

class ExceptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExceptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExceptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val exceptionText = intent.getStringExtra("exceptionText") as String

        binding.tvExceptionText.text = exceptionText
    }
}

