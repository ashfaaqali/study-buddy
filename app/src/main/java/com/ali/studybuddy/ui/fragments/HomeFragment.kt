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
import com.ali.studybuddy.viewmodel.SharedViewModel
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTabs()

        // Using shared viewmodel to share the current tab name
        sharedViewModel.setCurrentTabTitle(getTabName())

        binding.addSubjectFab.setOnClickListener {
            val day = getTabName()
            navigateToAddSubjectActivity(day)
        }
    }

    private fun navigateToAddSubjectActivity(day: String) {
        val intent = Intent(context, AddSubjectActivity::class.java)
        intent.putExtra(AppConstants.DAY, day)
        startActivity(intent)
    }

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

        adapter.addFragment(DayFragment(), "Monday")
        adapter.addFragment(DayFragment(), "Tuesday")
        adapter.addFragment(DayFragment(), "Wednesday")
        adapter.addFragment(DayFragment(), "Thursday")
        adapter.addFragment(DayFragment(), "Friday")
        adapter.addFragment(DayFragment(), "Saturday")

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                sharedViewModel.setCurrentTabTitle(tab.text.toString())
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}