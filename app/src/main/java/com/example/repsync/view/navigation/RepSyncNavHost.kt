package com.example.repsync.view.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.repsync.view.screens.FitScreen
import com.example.repsync.view.screens.WorkoutHistoryScreen

@Composable
fun RepSyncNavHost() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RepSyncRoute.Workout.route
    ) {

        composable(
            route = RepSyncRoute.Workout.route
        ) {

            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                FitScreen(
                    viewModel = hiltViewModel(),
                    onHistoryClick = {
                        navController.navigate(
                            RepSyncRoute.History.route
                        )
                    }
                )
            }
        }

        composable(
            route = RepSyncRoute.History.route
        ) {

            Surface(
                modifier = Modifier.fillMaxSize()
            ) {

                WorkoutHistoryScreen(
                    viewModel = hiltViewModel(),
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}