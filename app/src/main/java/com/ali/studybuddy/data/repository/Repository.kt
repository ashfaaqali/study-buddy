package com.ali.studybuddy.data.repository

import androidx.lifecycle.LiveData
import com.ali.studybuddy.data.dao.SubjectDAO
import com.ali.studybuddy.data.model.SubjectModel

class SubjectRepository(private val subjectDao: SubjectDAO) {

    fun getAllSubjects(): LiveData<List<SubjectModel>> {
        return subjectDao.getAllSubjects()
    }

    suspend fun addSubject(subject: SubjectModel) {
        subjectDao.addSubject(subject)
    }
}