package com.ali.studybuddy.viewmodel

import android.app.Application
import android.icu.util.Calendar
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.liveData
import com.ali.studybuddy.data.model.SubjectModel
import com.ali.studybuddy.data.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    application: Application,
    private val subjectRepository: SubjectRepository
) :
    AndroidViewModel(application) {

    // Using liveData builder to handle the suspend function
    val getAllSubjects: LiveData<List<SubjectModel>> = liveData {
        emit(subjectRepository.getAllSubjects())
    }
    private val _events = MutableLiveData<AddSubjectEvents>()
    val events: LiveData<AddSubjectEvents> get() = _events

    fun onAction(action: AddSubjectActions) {
        when (action) {
            is AddSubjectActions.TimePickerClicked -> {
                val calendar = Calendar.getInstance()
                _events.value = AddSubjectEvents.ShowTimePicker(
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE)
                )
            }

            is AddSubjectActions.SaveSubject -> {
                if (action.name.isNotEmpty()) {
                    saveSubjectDetailsToDb(
                        action.day,
                        action.name,
                        action.requiredAttendance,
                        action.timeInMillis
                    )
                } else {
                    _events.value = AddSubjectEvents.ShowToast("Subject name cannot be empty")
                }
            }
        }
    }

    private fun saveSubjectDetailsToDb(
        day: String,
        name: String,
        requiredAttendance: Int?,
        timeInMillis: Long?
    ) {
        viewModelScope.launch {
            try {
                val subject = SubjectModel(
                    id = 0,
                    day = day,
                    subjectName = name,
                    presentCount = 0,
                    absentCount = 0,
                    cancelledCount = 0,
                    requiredAttendance = requiredAttendance,
                    timeOfClass = timeInMillis
                )
                subjectRepository.addSubject(subject)
                _events.value = AddSubjectEvents.ShowToast("Subject saved successfully")
            } catch (e: Exception) {
                // Added error handling to catch and display any exceptions that occur during the database operation
                _events.value = AddSubjectEvents.ShowToast("Error saving subject: ${e.message}")
            }
        }
    }

    fun updateFormattedTime(formattedTime: String, timeInMillis: Long) {
        _events.value = AddSubjectEvents.ShowFormattedTime(formattedTime, timeInMillis)
    }

    fun getSubjectsForDay(day: String): LiveData<List<SubjectModel>> {
        return liveData {
            emit(subjectRepository.getSubjectsForTheDay(day))
        }
    }

}

sealed class AddSubjectEvents {
    data class ShowTimePicker(val currentHour: Int, val currentMinute: Int) : AddSubjectEvents()
    data class ShowFormattedTime(val formattedTime: String, val timeInMillis: Long) :
        AddSubjectEvents()

    data class ShowToast(val message: String) : AddSubjectEvents()
}

sealed class AddSubjectActions {
    data object TimePickerClicked : AddSubjectActions()
    data class SaveSubject(
        val day: String,
        val name: String,
        val requiredAttendance: Int?,
        val timeInMillis: Long?
    ) : AddSubjectActions()
}
