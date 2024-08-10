package com.ali.studybuddy.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.ali.studybuddy.R
import com.ali.studybuddy.databinding.SubjectInfoBottomSheetLayoutBinding
import com.ali.studybuddy.viewmodel.SubjectInfoActions
import com.ali.studybuddy.viewmodel.SubjectInfoBottomSheetViewModel
import com.ali.studybuddy.viewmodel.SubjectInfoEvents
import com.ali.studybuddy.util.AppConstants
import com.ali.studybuddy.viewmodel.DayActions
import com.ali.studybuddy.viewmodel.DayViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubjectInfoBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: SubjectInfoBottomSheetLayoutBinding
    private var subjectId: Long = 0
    private var day: String? = null
    private val viewModel: DayViewModel by viewModels()
    private var presentClassesCount: Int = 0
    private var absentClassesCount: Int = 0
    private var cancelledClassesCount: Int = 0
    private var totalClassesCount: Int = 0
    private var currentAttendance: Int = 0
    private val subjectInfoBottomSheetViewModel: SubjectInfoBottomSheetViewModel by viewModels()
    private var dismissListener: OnBottomSheetDismissListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SubjectInfoBottomSheetLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setDataToViews()

        // Get dropdown options
        val languages = resources.getStringArray(R.array.attendance_options)

        // Setup array adapter and pass the options and item view
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, languages)

        // Set array adapter to the auto complete text view
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        // Observe the actions on the bottom sheet
        subjectInfoBottomSheetViewModel.actions.observe(viewLifecycleOwner) { action ->
            when (action) {
                is SubjectInfoActions.ShowToast -> {
                    showToast(action.message)
                }
            }
        }

        binding.submitButton.setOnClickListener {
            val value = binding.autoCompleteTextView.text.toString()

            when {
                value.equals(AppConstants.MARK_PRESENT, ignoreCase = true) -> {
                    // Handle MARK_PRESENT case
                    // Trigger the onEvent for 'Mark Present'
                    subjectInfoBottomSheetViewModel.onEvent(SubjectInfoEvents.MarkPresent, subjectId)
                }

                value.equals(AppConstants.MARK_ABSENT, ignoreCase = true) -> {
                    // Handle MARK_ABSENT case
                    // Trigger the onEvent for 'Mark Absent'
                    subjectInfoBottomSheetViewModel.onEvent(SubjectInfoEvents.MarkAbsent, subjectId)
                }

                value.equals(AppConstants.MARK_CANCELLED, ignoreCase = true) -> {
                    // Handle MARK_CANCELLED case
                    // Trigger the onEvent for 'Mark Cancelled'
                    subjectInfoBottomSheetViewModel.onEvent(SubjectInfoEvents.MarkCancelled, subjectId)

                }
            }
            // viewModel.onAction(DayActions.DismissBottomSheet)
            dismiss()
        }
    }

    // Call this method to set the listener
    fun setDismissListener(listener: OnBottomSheetDismissListener) {
        dismissListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissListener?.onDismiss() // Notify the fragment when the bottom sheet is dismissed
    }

    private fun setDataToViews() {
        binding.apply {
            countPresentClasses.text = presentClassesCount.toString()
            countAbsentClasses.text = absentClassesCount.toString()
            countCancelledClasses.text = cancelledClassesCount.toString()
            countTotalClasses.text = totalClassesCount.toString()
        }
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(
            subjectId: Long,
            day: String,
            presentClassesCount: Int,
            absentClassesCount: Int,
            cancelledClassesCount: Int,
            totalClassesCount: Int,
            currentAttendance: Int
        ): SubjectInfoBottomSheet {
            val fragment = SubjectInfoBottomSheet()
            fragment.subjectId = subjectId
            fragment.day = day
            fragment.presentClassesCount = presentClassesCount
            fragment.absentClassesCount = absentClassesCount
            fragment.cancelledClassesCount = cancelledClassesCount
            fragment.totalClassesCount = totalClassesCount
            fragment.currentAttendance = currentAttendance
            return fragment
        }
    }
}

interface OnBottomSheetDismissListener {
    fun onDismiss()
}