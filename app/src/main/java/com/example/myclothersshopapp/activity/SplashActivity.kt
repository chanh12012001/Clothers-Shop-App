package com.example.myclothersshopapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.myclothersshopapp.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        @Suppress("DEPRECATION")
        Handler().postDelayed(
                {
                    //Launch the MainActivity
                    startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
                    finish() //Call this when your Activity is done and should be closed
        }, 2500)
    }
}
