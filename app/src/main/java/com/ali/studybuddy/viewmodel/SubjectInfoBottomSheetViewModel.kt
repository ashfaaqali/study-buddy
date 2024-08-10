package com.ali.studybuddy.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ali.studybuddy.data.model.SubjectModel
import com.ali.studybuddy.data.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectInfoBottomSheetViewModel @Inject constructor (
    application: Application,
    private val subjectRepository: SubjectRepository
) : AndroidViewModel(application) {
    private val _actions = MutableLiveData<SubjectInfoActions>()
    val actions: LiveData<SubjectInfoActions> get() = _actions

    fun onEvent(event: SubjectInfoEvents, subjectId: Long) {
        viewModelScope.launch {
            val subject = subjectRepository.getSubjectById(subjectId)
            if (subject != null) {
                when (event) {
                    is SubjectInfoEvents.MarkPresent -> {
                        incrementPresentCount(subject)
                    }
                    is SubjectInfoEvents.MarkAbsent -> {
                        incrementAbsentCount(subject)
                    }
                    is SubjectInfoEvents.MarkCancelled -> {
                        incrementCancelledCount(subject)
                    }
                }
            } else {
                _actions.value = SubjectInfoActions.ShowToast("Subject not found")
            }
        }
    }

    // Increment the count of Present classes for a subject
    private fun incrementPresentCount(subject: SubjectModel) {
        viewModelScope.launch {
            subjectRepository.updateSubject(subject.copy(presentCount = subject.presentCount + 1))
            _actions.value = SubjectInfoActions.ShowToast("Marked Present")
        }
    }

    // Increment the count of Absent classes for a subject
    private fun incrementAbsentCount(subject: SubjectModel) {
        viewModelScope.launch {
            subjectRepository.updateSubject(subject.copy(absentCount = subject.absentCount + 1))
            _actions.value = SubjectInfoActions.ShowToast("Marked Absent")
        }
    }

    // Increment the count of Cancelled classes for a subject
    private fun incrementCancelledCount(subject: SubjectModel) {
        viewModelScope.launch {
            subjectRepository.updateSubject(subject.copy(cancelledCount = subject.cancelledCount + 1))
            _actions.value = SubjectInfoActions.ShowToast("Marked Cancelled")
        }
    }
}

sealed class SubjectInfoEvents {
    data object MarkPresent : SubjectInfoEvents()
    data object MarkAbsent : SubjectInfoEvents()
    data object MarkCancelled : SubjectInfoEvents()
}

sealed class SubjectInfoActions {
    data class ShowToast(val message: String) : SubjectInfoActions()
}