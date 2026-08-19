package com.example.repsync.di

import android.content.Context
import androidx.room.Room
import com.example.repsync.data.local.RepSyncDatabase
import com.example.repsync.data.local.WorkoutDao
import com.example.repsync.data.sensor.StepCounter
import com.example.repsync.repository.FitnessRepository
import com.example.repsync.repository.FitnessRepositoryImpl
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
    fun provideStepCounter(
        @ApplicationContext context: Context
    ): StepCounter {
        return StepCounter(context)
    }
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RepSyncDatabase {
        return Room.databaseBuilder(
            context,
            RepSyncDatabase::class.java,
            "repsync_database"
        ).build()
    }

    @Provides
    fun provideWorkoutDao(database: RepSyncDatabase): WorkoutDao {
        return database.workoutDao()
    }

    @Provides
    @Singleton
    fun provideFitnessRepository(
        stepCounter: StepCounter,
        workoutDao: WorkoutDao
    ): FitnessRepository {
        return FitnessRepositoryImpl(
            stepCounter = stepCounter,
            workoutDao = workoutDao
        )
    }
}