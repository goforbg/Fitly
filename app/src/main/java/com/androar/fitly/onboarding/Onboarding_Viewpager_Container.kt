package com.androar.fitly.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.androar.fitly.ui.activity.SplashActivity
import com.google.android.material.tabs.TabLayout


/**
 * A simple Viewpager container for swiping onboarding pages
 */
class Onboarding_Viewpager_Container : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(com.androar.fitly.R.layout.fragment_onboarding_viewpager, container, false)
        val vpPager = view.findViewById(com.androar.fitly.R.id.vpOnboarding) as ViewPager
        val adapterViewPager =
            MyPagerAdapter(
                activity!!.getSupportFragmentManager()
            )
        vpPager.adapter = adapterViewPager

        val tabLayout: TabLayout =
            view.findViewById<View>(com.androar.fitly.R.id.tab_layout) as TabLayout
        tabLayout.setupWithViewPager(vpPager, true)

        val startButton  = view.findViewById<TextView>(com.androar.fitly.R.id.startButton)
        startButton.setOnClickListener {
            if (vpPager.currentItem!=2) {
                vpPager.arrowScroll(View.FOCUS_RIGHT)
            }
            else {
                startActivity(Intent(activity!!, SplashActivity::class.java))
                activity!!.finish()
            }
        }
        return view
    }
    class MyPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
    {
        val fragment1 = OnboardingPageOne()
        val fragment2 = OnboardingPageTwo()
        val fragment3 = OnboardingPageThree()
        // Returns total number of pages.
        override fun getCount(): Int {
            return NUM_ITEMS
        }

        // Returns the fragment to display for a particular page.
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> fragment1
                1 -> fragment2
                2 -> fragment3
                else -> fragment1
            }

        }

        companion object {
            private const val NUM_ITEMS = 3
        }
    }


}
