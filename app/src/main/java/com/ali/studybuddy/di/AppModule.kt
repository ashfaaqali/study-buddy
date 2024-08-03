package com.ali.studybuddy.di

import android.content.Context
import com.ali.studybuddy.data.dao.SubjectDAO
import com.ali.studybuddy.data.database.AppDatabase
import com.ali.studybuddy.data.repository.SubjectRepository
import com.ali.studybuddy.data.repository.SubjectRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideSubjectDao(appDatabase: AppDatabase): SubjectDAO {
        return appDatabase.subjectDao()
    }

    @Provides
    @Singleton
    fun provideSubjectRepository(subjectDao: SubjectDAO): SubjectRepository {
        return SubjectRepositoryImpl(subjectDao)
    }
}