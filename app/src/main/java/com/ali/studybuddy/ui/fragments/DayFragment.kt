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
import com.ali.studybuddy.ui.bottomsheet.OnBottomSheetDismissListener
import com.ali.studybuddy.ui.bottomsheet.SubjectInfoBottomSheet
import com.ali.studybuddy.viewmodel.DayActions
import com.ali.studybuddy.viewmodel.DayEvents
import com.ali.studybuddy.viewmodel.DayViewModel
import com.ali.studybuddy.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayFragment : Fragment(), OnBottomSheetDismissListener {
    private var subjectInfoBottomSheet : SubjectInfoBottomSheet? = null
    private lateinit var binding: FragmentDayBinding
    private val viewModel: DayViewModel by viewModels()
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
            override fun onItemClickListener(
                subjectId: Long,
                day: String,
                presentCount: Int,
                absentCount: Int,
                cancelledCount: Int,
                totalClasses: Int,
                currentAttendance: Int
            ) {
                viewModel.onAction(
                    DayActions.ShowBottomSheet(
                        subjectId,
                        day,
                        presentCount,
                        absentCount,
                        cancelledCount,
                        totalClasses,
                        currentAttendance
                    )
                )
            }
        }

        viewModel.dayEvents.observe(viewLifecycleOwner) { event ->
            when (event) {
                is DayEvents.ShowBottomSheet -> {
                    showBottomSheet(
                        event.subjectId,
                        event.day,
                        event.presentCount,
                        event.absentCount,
                        event.cancelledCount,
                        event.totalClasses,
                        event.currentAttendance
                    )
                }

                /* is DayEvents.DismissBottomSheet -> {
                    Log.d("TAG", "onViewCreated: DismissBottomSheet")
                    dismissBottomSheet()
                } */

                is DayEvents.RefreshData -> {
                    observeSubjectChanges()
                }
            }
        }
    }

    private fun dismissBottomSheet() {
        subjectInfoBottomSheet?.dismiss()
        subjectInfoBottomSheet = null
    }

    private fun showBottomSheet(
        subjectId: Long,
        day: String,
        presentCount: Int,
        absentCount: Int,
        cancelledCount: Int,
        totalClasses: Int,
        currentAttendance: Int
    ) {
        subjectInfoBottomSheet = SubjectInfoBottomSheet.newInstance(
            subjectId,
            day,
            presentCount,
            absentCount,
            cancelledCount,
            totalClasses,
            currentAttendance
        )
        subjectInfoBottomSheet!!.setDismissListener(this@DayFragment)
        subjectInfoBottomSheet!!.show(parentFragmentManager, "Tag")
    }

    override fun onResume() {
        super.onResume()
        observeSubjectChanges()
    }

    private fun observeSubjectChanges() {
        sharedViewModel.currentTabTitle.observe(viewLifecycleOwner) { title ->
            viewModel.getSubjectsForDay(title).observe(viewLifecycleOwner) { subjects ->
                adapter.updateData(subjects)
            }
        }
    }

    override fun onDismiss() {
        viewModel.onAction(DayActions.RefreshData)
    }
}