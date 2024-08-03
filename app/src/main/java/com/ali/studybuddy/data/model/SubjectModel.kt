package com.ali.studybuddy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subject")
data class SubjectModel (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val day: String,
    val subjectName: String,
    val presentCount: Int = 0,
    val absentCount: Int = 0,
    val cancelledCount: Int = 0,
    val requiredAttendance: Int?,
    val timeOfClass: Long?
)