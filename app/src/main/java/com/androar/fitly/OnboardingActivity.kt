package com.androar.fitly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        val fragment = Onboarding_Viewpager_Container()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.layout_onboarding, fragment)
            .commit();
    }
}
