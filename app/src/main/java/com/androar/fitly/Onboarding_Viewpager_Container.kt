package com.androar.fitly

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


/**
 * A simple [Fragment] subclass.
 */
class Onboarding_Viewpager_Container : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(com.androar.fitly.R.layout.fragment_onboarding_viewpager, container, false)
        val vpPager = view.findViewById(com.androar.fitly.R.id.vpOnboarding) as ViewPager
        val adapterViewPager = MyPagerAdapter(activity!!.getSupportFragmentManager())
        vpPager.adapter = adapterViewPager

        val tabLayout: TabLayout =
            view.findViewById<View>(com.androar.fitly.R.id.tab_layout) as TabLayout
        tabLayout.setupWithViewPager(vpPager, true)


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

        // Returns the page title for the top indicator
        override fun getPageTitle(position: Int): CharSequence? {
            return ""
        }

        companion object {
            private const val NUM_ITEMS = 3
        }
    }


}
