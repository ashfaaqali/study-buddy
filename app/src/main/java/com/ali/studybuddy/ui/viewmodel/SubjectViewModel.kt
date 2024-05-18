package com.ali.studybuddy.ui.viewmodel

import android.app.Application
import android.icu.util.Calendar
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ali.studybuddy.data.database.AppDatabase
import com.ali.studybuddy.data.model.SubjectModel
import com.ali.studybuddy.data.repository.SubjectRepository
import kotlinx.coroutines.launch

class SubjectViewModel(application: Application) : AndroidViewModel(application) {
    val getAllSubjects: LiveData<List<SubjectModel>>
    private val subjectRepository: SubjectRepository
    private val _events = MutableLiveData<AddSubjectEvents>()
    val events: LiveData<AddSubjectEvents> get() = _events

    init {
        val subjectDao = AppDatabase.getDatabase(application).subjectDao()
        subjectRepository = SubjectRepository(subjectDao)
        getAllSubjects = subjectRepository.getAllSubjects()
    }

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

    private fun saveSubjectDetailsToDb(name: String, requiredAttendance: Int?, timeInMillis: Long?) {
        viewModelScope.launch {
            val subject = SubjectModel(0, name, requiredAttendance, timeInMillis)
            subjectRepository.addSubject(subject)
        }
    }

    fun updateFormattedTime(formattedTime: String, timeInMillis: Long) {
        _events.value = AddSubjectEvents.ShowFormattedTime(formattedTime, timeInMillis)
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
        val name: String,
        val requiredAttendance: Int?,
        val timeInMillis: Long?
    ) : AddSubjectActions()
}
