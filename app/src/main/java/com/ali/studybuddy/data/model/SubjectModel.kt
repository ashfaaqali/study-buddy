package com.ali.studybuddy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subject")
data class SubjectModel (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val day: String,
    val subjectName: String,
    val requiredAttendance: Int?,
    val timeOfClass: Long?
)