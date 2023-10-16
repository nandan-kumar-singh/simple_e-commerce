package com.example.alsess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.alsess.databinding.ActivitySingUpBinding

class SingUp : AppCompatActivity() {
    private lateinit var viewBinding : ActivitySingUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        viewBinding = ActivitySingUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}