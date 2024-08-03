package com.ali.studybuddy.ui.bottomsheet

import android.os.Bundle
import android.util.Log
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubjectInfoBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: SubjectInfoBottomSheetLayoutBinding
    private var subjectId: Long = 0
    private val subjectInfoBottomSheetViewModel: SubjectInfoBottomSheetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SubjectInfoBottomSheetLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (arguments != null) {
            /// requireArguments().getString(AppConstants.DAY)
            subjectId = requireArguments().getLong(AppConstants.SUBJECT_ID)
        } else {
            showToast("Error: Arguments are missing")
            dismiss() // Dismiss the bottom sheet if the arguments are null
        }

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
                    Log.d("TEST 1", "onViewCreated: SUBMIT BUTTON CLICKED; VALUE: Mark Present SUBJECT ID : $subjectId")
                    // Trigger the onEvent for 'Mark Present'
                    subjectInfoBottomSheetViewModel.onEvent(SubjectInfoEvents.MarkPresent, subjectId)
                }

                value.equals(AppConstants.MARK_ABSENT, ignoreCase = true) -> {
                    // Handle MARK_ABSENT case
                    Log.d("TEST 1", "onViewCreated: SUBMIT BUTTON CLICKED; VALUE: Mark Absent SUBJECT ID : $subjectId")

                    // Trigger the onEvent for 'Mark Absent'
                    subjectInfoBottomSheetViewModel.onEvent(SubjectInfoEvents.MarkAbsent, subjectId)
                }

                value.equals(AppConstants.MARK_CANCELLED, ignoreCase = true) -> {
                    // Handle MARK_CANCELLED case
                    Log.d("TEST 1", "onViewCreated: SUBMIT BUTTON CLICKED; VALUE: Mark Cancelled SUBJECT ID : $subjectId")
                    // Trigger the onEvent for 'Mark Cancelled'
                    subjectInfoBottomSheetViewModel.onEvent(SubjectInfoEvents.MarkCancelled, subjectId)
                }
            }
        }
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun newInstance(
        /*presentClassesCount: Int,
        cancelledClassesCount: Int,
        absentClassesCount: Int,
        totalClassesCount: Int*/
        subjectId: Long,
        day: String
    ): SubjectInfoBottomSheet {
        val fragment = SubjectInfoBottomSheet()
        val args = Bundle()
        /*args.putInt(ARG_PRESENT_CLASSES_COUNT, presentClassesCount)
        args.putInt(ARG_CANCELLED_CLASSES_COUNT, cancelledClassesCount)
        args.putInt(ARG_ABSENT_CLASSES_COUNT, absentClassesCount)
        args.putInt(ARG_TOTAL_CLASSES_COUNT, totalClassesCount)*/
        args.putString(AppConstants.DAY, day)
        args.putLong(AppConstants.SUBJECT_ID, subjectId)
        fragment.arguments = args
        return fragment
    }
}