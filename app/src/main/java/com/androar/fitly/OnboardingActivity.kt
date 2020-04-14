package com.androar.fitly

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val sharedPref = AppPreferences(this).Editor()
        sharedPref.putBoolean(getString(R.string.onboarding_seen), true)
        sharedPref.commit()

        val fragment = Onboarding_Viewpager_Container()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.layout_onboarding, fragment)
            .commit();
    }
}
