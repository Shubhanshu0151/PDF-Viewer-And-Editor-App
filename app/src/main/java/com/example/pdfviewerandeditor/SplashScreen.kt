package com.example.pdfviewerandeditor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.pdfviewerandeditor.UserRegistration.LogIn


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        Handler().postDelayed({
            val logo = Intent(this,LogIn::class.java)
            startActivity(logo)
            finish()
        },3000)
    }
}