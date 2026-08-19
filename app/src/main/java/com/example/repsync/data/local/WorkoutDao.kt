package com.example.repsync.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Query("SELECT * FROM workouts ORDER BY startTime DESC")
    fun observeWorkouts(): Flow<List<WorkoutEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity)
    @Query("DELETE FROM workouts")
    suspend fun clearWorkouts()
}