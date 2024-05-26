package com.ali.studybuddy.ui.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.ui.setupWithNavController
import com.ali.studybuddy.R
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.ali.studybuddy.databinding.ActivityMainBinding
import com.ali.studybuddy.ui.fragments.BuddyFragment
import com.ali.studybuddy.ui.fragments.HomeFragment
import com.ali.studybuddy.ui.fragments.MoreFragment

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(HomeFragment())
        handleBackPress()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        // Handle bottom navigation item selection
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    setFragment(HomeFragment())
                    true
                }

                R.id.assistant -> {
                    setFragment(BuddyFragment())
                    true
                }

                R.id.settings -> {
                    setFragment(MoreFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}