package com.ali.studybuddy.data.repository

import androidx.lifecycle.LiveData
import com.ali.studybuddy.data.dao.SubjectDAO
import com.ali.studybuddy.data.model.SubjectModel
import javax.inject.Inject

class SubjectRepositoryImpl @Inject constructor(private val subjectDao: SubjectDAO): SubjectRepository {
    override suspend fun getAllSubjects(): List<SubjectModel> {
        return subjectDao.getAllSubjects()
    }

    override suspend fun getSubjectsForTheDay(day: String): List<SubjectModel> {
        return subjectDao.getSubjectsForTheDay(day)
    }

    override suspend fun addSubject(subject: SubjectModel) {
        subjectDao.addSubject(subject)
    }

    override suspend fun getSubject(day: String, subjectName: String): SubjectModel? {
        return subjectDao.getSubject(day, subjectName)
    }

    override suspend fun getSubjectById(subjectId: Long): SubjectModel? {
        return subjectDao.getSubjectById(subjectId)
    }

    override suspend fun updateSubject(subject: SubjectModel) {
        subjectDao.updateSubject(subject)
    }
}