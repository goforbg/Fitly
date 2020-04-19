package com.androar.fitly.Onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androar.fitly.AppPreferences
import com.androar.fitly.Onboarding.Onboarding_Viewpager_Container
import com.androar.fitly.R


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
