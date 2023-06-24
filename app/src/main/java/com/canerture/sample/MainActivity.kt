package com.canerture.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.canerture.exceptionreport.ui.ExceptionReport
import com.canerture.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ExceptionReport(this, R.color.teal_700)
            //.setCustomActivity(CustomExceptionActivity::class.java)
            //.enableSolutionModule(BuildConfig.API_KEY)

        binding.btnException.setOnClickListener {
            throw NullPointerException("Null data!")
        }
    }
}