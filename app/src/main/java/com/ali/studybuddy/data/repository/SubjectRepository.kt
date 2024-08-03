package com.ali.studybuddy.data.repository

import androidx.lifecycle.LiveData
import com.ali.studybuddy.data.dao.SubjectDAO
import com.ali.studybuddy.data.model.SubjectModel

interface SubjectRepository {

    suspend fun getAllSubjects(): List<SubjectModel>

    suspend fun getSubjectsForTheDay(day: String): List<SubjectModel>

    suspend fun addSubject(subject: SubjectModel)

    suspend fun getSubject(day: String, subjectName: String): SubjectModel?

    suspend fun getSubjectById(subjectId: Long): SubjectModel?

    suspend fun updateSubject(subject: SubjectModel)
}