package com.androar.fitly

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * A simple [Fragment] subclass.
 */
class OnboardingPageThree : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_onboarding_three, container, false)

        val btnStart = view.findViewById<TextView>(R.id.startButton)
        btnStart.setOnClickListener {

            startActivity(Intent(activity!!, SplashActivity::class.java))
            activity!!.finish()
        }

        return view
    }

}
