package com.sunshine.freeform.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.sunshine.freeform.R
import com.sunshine.freeform.databinding.ActivityMainBinding
import com.sunshine.freeform.ui.choose_apps.ChooseAppsFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.viewPager.apply {
            adapter = object : FragmentStateAdapter(this@MainActivity) {
                override fun getItemCount(): Int {
                    return 3
                }

                override fun createFragment(position: Int): Fragment {
                    return when(position) {
                        0 -> HomeFragment()
                        1 -> ChooseAppsFragment()
                        else -> SettingFragment()
                    }
                }
            }
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.navView.menu.getItem(position).isChecked = true
                }
            })
            isUserInputEnabled = false
            offscreenPageLimit = 2
        }
        binding.navView.apply {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        binding.viewPager.currentItem = 0
                    }
                    R.id.navigation_apps -> {
                        binding.viewPager.currentItem = 1
                    }
                    else ->  {
                        binding.viewPager.currentItem = 2
                    }
                }
                true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun changeToSetting() {
        binding.viewPager.currentItem = 1
    }
}