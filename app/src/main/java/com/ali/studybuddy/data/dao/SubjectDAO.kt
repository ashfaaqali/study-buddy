package com.ali.studybuddy.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ali.studybuddy.data.model.SubjectModel

@Dao
interface SubjectDAO {

    @Query("SELECT * FROM subject ORDER BY timeOfClass")
    suspend fun getAllSubjects(): List<SubjectModel>

    @Query("SELECT * FROM subject WHERE day = :day")
    suspend fun getSubjectsForTheDay(day: String): List<SubjectModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSubject(subject: SubjectModel)

    @Query("SELECT * FROM subject WHERE day = :day AND subjectName = :subjectName LIMIT 1")
    suspend fun getSubject(day: String, subjectName: String): SubjectModel?

    @Query("SELECT * FROM subject WHERE id = :subjectId LIMIT 1")
    suspend fun getSubjectById(subjectId: Long): SubjectModel?

    @Update
    suspend fun updateSubject(subject: SubjectModel)
}