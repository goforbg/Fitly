package com.androar.fitly.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.androar.fitly.R
import com.androar.fitly.ui.fragment.HomeFragment
import com.androar.fitly.ui.fragment.TrendingFragment
import com.androar.fitly.ui.fragment.ProfileFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy


class MainActivity : AppCompatActivity() {

    val titles =
        arrayOf("Home", "Trending", "Profile")

    val icons =
        arrayOf(
            R.drawable.ic_home_selector,
            R.drawable.ic_trending_selector,
            R.drawable.ic_profile_selector
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        viewPager.setAdapter(
            ViewPagerFragmentAdapter(
                this
            )
        )
        TabLayoutMediator(tabLayout, viewPager,
            TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                tab.setIcon(icons[position])
            }
        ).attach()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

            }
        })


    }


    private class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        val titles =
            arrayOf("Home", "Trending", "Profile")

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return HomeFragment()
                1 -> return TrendingFragment()
                2 -> return ProfileFragment()
            }
            return HomeFragment()
        }

        override fun getItemCount(): Int {
            return titles.size
        }
    }

}


