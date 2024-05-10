package com.ali.studybuddy.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ali.studybuddy.R
import com.ali.studybuddy.databinding.FragmentDayBinding
import com.ali.studybuddy.util.AppConstants

class DayFragment : Fragment() {
    private lateinit var binding: FragmentDayBinding
    private var data: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDayBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleArguments()
        setData()
    }

    private fun setData() {
        binding.textView.text = data
    }

    private fun handleArguments() {
        if (arguments!=null){
            data = requireArguments().getString(AppConstants.DAY)
        }
    }
}