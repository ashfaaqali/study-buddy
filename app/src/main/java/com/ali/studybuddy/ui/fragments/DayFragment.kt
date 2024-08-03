package com.ali.studybuddy.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ali.studybuddy.databinding.FragmentDayBinding
import com.ali.studybuddy.ui.adapter.DayAdapter
import com.ali.studybuddy.ui.adapter.OnItemClickListener
import com.ali.studybuddy.ui.bottomsheet.SubjectInfoBottomSheet
import com.ali.studybuddy.viewmodel.SharedViewModel
import com.ali.studybuddy.viewmodel.SubjectViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayFragment : Fragment() {
    private val subjectInfoBottomSheet by lazy {
        SubjectInfoBottomSheet()
    }
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
        adapter.clickListener = object : OnItemClickListener {
            override fun onItemClickListener(subjectId: Long, day: String) {
                val subjectInfoBottomSheet = subjectInfoBottomSheet.newInstance(subjectId, day)
                subjectInfoBottomSheet.show(parentFragmentManager, "Tag")
            }
        }

        sharedViewModel.currentTabTitle.observe(viewLifecycleOwner) { title ->
            viewModel.getSubjectsForDay(title).observe(viewLifecycleOwner) { subjects ->
                adapter.updateData(subjects)
                for (i in subjects){
                    Log.d("DayFragment", "Subject : $i")
                }
            }
        }
    }
}