package com.ali.studybuddy.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.ali.studybuddy.R
import com.ali.studybuddy.databinding.SubjectInfoBottomSheetLayoutBinding
import com.ali.studybuddy.util.AppConstants.ARG_ABSENT_CLASSES_COUNT
import com.ali.studybuddy.util.AppConstants.ARG_CANCELLED_CLASSES_COUNT
import com.ali.studybuddy.util.AppConstants.ARG_PRESENT_CLASSES_COUNT
import com.ali.studybuddy.util.AppConstants.ARG_TOTAL_CLASSES_COUNT
import com.ali.studybuddy.util.AppConstants.DAY
import com.ali.studybuddy.util.AppConstants.SUBJECT_NAME
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SubjectInfoBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: SubjectInfoBottomSheetLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SubjectInfoBottomSheetLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val languages = resources.getStringArray(R.array.attendance_options)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, languages)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.submitButton.setOnClickListener {

        }
    }

    fun newInstance(
        /*presentClassesCount: Int,
        cancelledClassesCount: Int,
        absentClassesCount: Int,
        totalClassesCount: Int*/
        subjectName: String,
        day: String
    ): SubjectInfoBottomSheet {
        val fragment = SubjectInfoBottomSheet()
        val args = Bundle()
        /*args.putInt(ARG_PRESENT_CLASSES_COUNT, presentClassesCount)
        args.putInt(ARG_CANCELLED_CLASSES_COUNT, cancelledClassesCount)
        args.putInt(ARG_ABSENT_CLASSES_COUNT, absentClassesCount)
        args.putInt(ARG_TOTAL_CLASSES_COUNT, totalClassesCount)*/
        args.putString(DAY, day)
        args.putString(SUBJECT_NAME, subjectName)
        fragment.arguments = args
        return fragment
    }
}