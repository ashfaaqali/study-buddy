package com.ali.studybuddy.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ali.studybuddy.databinding.FragmentDayBinding
import com.ali.studybuddy.ui.adapter.DayAdapter
import com.ali.studybuddy.ui.viewmodel.SharedViewModel
import com.ali.studybuddy.ui.viewmodel.SubjectViewModel

class DayFragment : Fragment() {
    private val TAG = "DayFragment"
    private lateinit var binding: FragmentDayBinding
    private val viewModel: SubjectViewModel by viewModels()
    private lateinit var adapter: DayAdapter
    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDayBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = DayAdapter(requireContext(), emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Test")
        sharedViewModel.currentTabTitle.observe(viewLifecycleOwner) { title ->
            Log.d(TAG, "onViewCreated: currentTab $title")
            viewModel.getSubjectsForDay(title).observe(viewLifecycleOwner) { subjects ->
                Log.d(TAG, "onViewCreated: Data for $title: $subjects")
                adapter.updateData(subjects)
            }
        }
    }
}