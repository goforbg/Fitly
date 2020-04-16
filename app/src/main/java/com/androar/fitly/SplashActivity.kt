package com.androar.fitly

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.androar.fitly.R.layout.activity_splash)

        Handler().postDelayed(Runnable { // This method will be executed once the timer is over
            val i = Intent(this@SplashActivity, VideoPlayerActivity::class.java)
            startActivity(i)
            finish()
        }, 500)
    }
}
