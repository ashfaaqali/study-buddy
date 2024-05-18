package com.ali.studybuddy.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ali.studybuddy.data.model.SubjectModel

@Dao
interface SubjectDAO {

    @Query("SELECT * FROM subject ORDER BY timeOfClass")
    fun getAllSubjects(): LiveData<List<SubjectModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSubject(subject: SubjectModel)
}