package com.ali.studybuddy.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.ali.studybuddy.R
import com.ali.studybuddy.databinding.FragmentHomeBinding
import com.ali.studybuddy.ui.adapter.HomeAdapter
import com.ali.studybuddy.util.AppConstants
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: HomeAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // navController = Navigation.findNavController(requireActivity(), R.id.view_pager)
        setUpTabs()

        val tabName = getTabName()
        if (tabName == AppConstants.MONDAY) startFragment(AppConstants.MONDAY)
    }

    private fun startFragment(day: String) {
        val bundle = Bundle()
        bundle.putString(AppConstants.DAY, day)
        navController.navigate(R.id.dayFragment, bundle)
    }

    // Get Current Tab Name
    private fun getTabName(): String {
        val currentTabIndex = tabLayout.selectedTabPosition
        return adapter.getTitle(currentTabIndex)
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
        }.attach()

        // adapter.notifyDataSetChanged()
    }
}