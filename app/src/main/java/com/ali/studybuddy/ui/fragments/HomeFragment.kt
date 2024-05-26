package com.ali.studybuddy.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.ali.studybuddy.databinding.FragmentHomeBinding
import com.ali.studybuddy.ui.activity.AddSubjectActivity
import com.ali.studybuddy.ui.adapter.HomeAdapter
import com.ali.studybuddy.ui.viewmodel.SharedViewModel
import com.ali.studybuddy.util.AppConstants
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: HomeAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedViewModel: SharedViewModel
    private val TAG = "HomeFragment"
    private var day: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpTabs()
        if (savedInstanceState == null) {
            // Set default tab to be selected
            tabLayout.getTabAt(0)?.select()
            day = getTabName()
            Log.d(TAG, "onViewCreated: Day: $day")
            sharedViewModel.setCurrentTabTitle(day ?: "Monday")  // Default to Monday if day is null
        }
        day = getTabName()
        // Log.d(TAG, "onViewCreated: Day: $day")
        binding.addSubjectFab.setOnClickListener {
            navigateToAddSubjectActivity()
        }
    }

    private fun navigateToAddSubjectActivity() {
        val intent = Intent(context, AddSubjectActivity::class.java)
        intent.putExtra(AppConstants.DAY, day)
        startActivity(intent)
    }

    // Get Current Tab Name
    private fun getTabName(): String {
        val currentTabIndex = tabLayout.selectedTabPosition
        return adapter.getTitle(currentTabIndex)
    }

    private fun startFragment(currentTab: String) {
        if (parentFragment != null) {
            val navController = requireParentFragment().findNavController()
            val action = HomeFragmentDirections.actionHomeFragmentToDayFragment(currentTab)
            Log.d(TAG, "startFragment: action: ${action.arguments}")
            navController.navigate(action)
        }
    }

    private fun setUpTabs() {
        tabLayout = binding.tabLayout
        viewPager = binding.viewPager
        adapter = HomeAdapter(requireActivity())

        // Add Tabs To TabLayout
        adapter.addFragment(DayFragment(), "Monday")
        adapter.addFragment(DayFragment(), "Tuesday")
        adapter.addFragment(DayFragment(), "Wednesday")
        adapter.addFragment(DayFragment(), "Thursday")
        adapter.addFragment(DayFragment(), "Friday")
        adapter.addFragment(DayFragment(), "Saturday")

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getTitle(position)
            Log.d(TAG, "setUpTabs: TabLayoutMediator: ${tab.text}")
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d(TAG, "setUpTabs: onTabSelectedListener: ${tab.text.toString()}")
                 sharedViewModel.setCurrentTabTitle(tab.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}