package com.ali.studybuddy.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.ali.studybuddy.databinding.FragmentHomeBinding
import com.ali.studybuddy.ui.adapter.HomeAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: HomeAdapter
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpTabs()
    }

    private fun setUpTabs() {
        tabLayout = binding.tabLayout
        viewPager = binding.viewPager
        adapter = HomeAdapter(requireActivity())

        adapter.addFragment(DayFragment(), "Home")
        adapter.addFragment(DayFragment(), "Updates")
        adapter.addFragment(DayFragment(), "Calls")
        adapter.notifyDataSetChanged()
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()
    }
}