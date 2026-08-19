package com.example.repsync.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.repsync.R
import com.example.repsync.view.components.cards.WorkoutHistoryCard
import com.example.repsync.viewmodel.FitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHistoryScreen(
    viewModel: FitViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {

    val workouts by viewModel.workoutHistory
        .collectAsStateWithLifecycle()

    var showClearDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            R.string.workout_history
                        ),
                        fontWeight = FontWeight.Bold
                    )
                },

                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription =
                                stringResource(R.string.back)
                        )
                    }
                },

                actions = {
                    if (workouts.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                showClearDialog = true
                            }
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.DeleteSweep,
                                contentDescription =
                                    stringResource(
                                        R.string.clear_history
                                    )
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        if (workouts.isEmpty()) {
            EmptyWorkoutHistory(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = workouts,
                    key = { it.id }
                ) { workout ->

                    WorkoutHistoryCard(
                        workout = workout
                    )
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = {
                showClearDialog = false
            },
            title = {
                Text(
                    text = stringResource(
                        R.string.clear_workout_history_title
                    )
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.clear_workout_history_message
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {

                        viewModel.clearWorkoutHistory()

                        showClearDialog = false
                    }
                ) {

                    Text(
                        text = stringResource(
                            R.string.clear
                        ),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showClearDialog = false
                    }
                ) {

                    Text(
                        text = stringResource(
                            R.string.cancel
                        )
                    )
                }
            }
        )
    }
}

@Composable
private fun EmptyWorkoutHistory(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "🏃",
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = stringResource(
                R.string.workout_pending
            ),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = stringResource(
                R.string.no_completed_workouts
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}