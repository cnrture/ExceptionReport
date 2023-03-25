package com.canerture.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.canerture.exceptionreport.ExceptionHandler
import com.canerture.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ExceptionHandler().init(this, R.color.teal_700)

        binding.btnException.setOnClickListener {
            throw NullPointerException("Null data!")
        }
    }
}