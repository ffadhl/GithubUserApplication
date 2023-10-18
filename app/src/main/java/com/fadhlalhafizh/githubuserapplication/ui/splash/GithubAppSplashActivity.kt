package com.fadhlalhafizh.githubuserapplication.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.fadhlalhafizh.githubuserapplication.R
import com.fadhlalhafizh.githubuserapplication.ui.main.MainActivity

class GithubAppSplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_app_splash)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }, 2000)

    }
}