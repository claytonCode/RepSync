package com.example.repsync.view.navigation

sealed class RepSyncRoute(
    val route: String
) {
    data object Workout : RepSyncRoute("workout")

    data object History : RepSyncRoute("history")
}