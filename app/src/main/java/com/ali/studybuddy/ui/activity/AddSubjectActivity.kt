package com.ali.studybuddy.ui.activity

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ali.studybuddy.data.model.SubjectModel
import com.ali.studybuddy.databinding.ActivityAddSubjectBinding
import com.ali.studybuddy.ui.viewmodel.AddSubjectActions
import com.ali.studybuddy.ui.viewmodel.AddSubjectEvents
import com.ali.studybuddy.ui.viewmodel.SubjectViewModel
import com.ali.studybuddy.util.AppConstants
import java.util.*

class AddSubjectActivity : AppCompatActivity() {
    private var timeInMillieSeconds = 0L
    private lateinit var binding: ActivityAddSubjectBinding
    private lateinit var subjectViewModel: SubjectViewModel
    private lateinit var day: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subjectViewModel = SubjectViewModel(application)

        handleIntentData() // Handle the intent data

        binding.timePicker.setOnClickListener {// Time picker
            subjectViewModel.onAction(AddSubjectActions.TimePickerClicked)
        }

        subjectViewModel.events.observe(this){ events ->
            handleEvents(events)
        }

        binding.saveBtn.setOnClickListener {
            binding.apply {
                val subjectName = subjectNameEditText.text.toString()
                val subjectRequiredAttendance = requiredAttendanceEditText.text.toString().toIntOrNull()
                val classTime =
                    if (timePicker.text.equals("Select time")) null else timeInMillieSeconds

                subjectViewModel.onAction(
                    AddSubjectActions.SaveSubject(
                        subjectName,
                        subjectRequiredAttendance,
                        classTime
                    )
                )
            }
        }
    }

    // Handle the events triggered from viewmodel here
    private fun handleEvents(event: AddSubjectEvents) {
        when (event) {
            is AddSubjectEvents.ShowTimePicker -> {
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _: TimePicker, hourOfDay: Int, minute: Int ->

                        val calendar = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                        }

                        val formattedTime = String.format(
                            Locale.getDefault(), "%02d:%02d %s",
                            if (hourOfDay > 12) hourOfDay - 12 else hourOfDay,
                            minute,
                            if (hourOfDay >= 12) "PM" else "AM"
                        )
                        // Trigger the update formatted time event in the viewmodel
                        subjectViewModel.updateFormattedTime(formattedTime, calendar.timeInMillis)
                    },
                    event.currentHour,
                    event.currentMinute,
                    false
                )
                timePickerDialog.show()
            }

            is AddSubjectEvents.ShowFormattedTime -> {
                binding.timePicker.text = event.formattedTime
                timeInMillieSeconds = event.timeInMillis
            }

            is AddSubjectEvents.ShowToast -> {
                Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleIntentData() {
        if (intent != null) {
            day = intent.getStringExtra(AppConstants.DAY).toString()
        }
    }
}
