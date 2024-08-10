package com.ali.studybuddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.ali.studybuddy.data.model.SubjectModel
import com.ali.studybuddy.data.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(
    application: Application,
    private val subjectRepository: SubjectRepository
) : AndroidViewModel(application) {

    private val _dayEvents = MutableLiveData<DayEvents>()
    val dayEvents: LiveData<DayEvents> get() = _dayEvents

    fun getSubjectsForDay(day: String): LiveData<List<SubjectModel>> {
        return liveData {
            emit(subjectRepository.getSubjectsForTheDay(day))
        }
    }

    fun onAction(actions: DayActions) {
        when (actions) {
            is DayActions.ShowBottomSheet -> {
                _dayEvents.value = DayEvents.ShowBottomSheet(
                    actions.subjectId,
                    actions.day,
                    actions.presentCount,
                    actions.absentCount,
                    actions.cancelledCount,
                    actions.totalClasses,
                    actions.currentAttendance
                )
            }

            /*is DayActions.DismissBottomSheet -> {
                Log.d("TAG", "onAction: DismissBottomSheet")
                _dayEvents.value = DayEvents.DismissBottomSheet
            }*/

            is DayActions.RefreshData -> {
                _dayEvents.value = DayEvents.RefreshData
            }
        }
    }
}

sealed class DayEvents {
    data class ShowBottomSheet(
        var subjectId: Long,
        var day: String,
        var presentCount: Int,
        var absentCount: Int,
        var cancelledCount: Int,
        var totalClasses: Int,
        var currentAttendance: Int
    ) : DayEvents()

    // data object DismissBottomSheet : DayEvents()
    data object RefreshData : DayEvents()
}

sealed class DayActions {
    data class ShowBottomSheet(
        var subjectId: Long,
        var day: String,
        var presentCount: Int,
        var absentCount: Int,
        var cancelledCount: Int,
        var totalClasses: Int,
        var currentAttendance: Int
    ) : DayActions()

    // data object DismissBottomSheet : DayActions()
    data object RefreshData : DayActions()
}
